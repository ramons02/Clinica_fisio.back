import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ClinicaService } from '../services/clinica.service';

@Component({
  selector: 'app-pacientes',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './pacientes.component.html',
  styleUrl: './pacientes.component.scss'
})
export class PacientesComponent implements OnInit {

  telaAtual: string = 'pacientes';
  listaPacientes: any[] = [];
  pacienteSelecionado: any = null;

  // Evoluções
  listaEvolucoes: any[] = [];
  novaEvolucao: string = '';

  // Cadastro e Upload
  arquivoSelecionado: File | null = null;
  novoPaciente: any = this.resetForm();

  constructor(private clinicaService: ClinicaService) {}

  ngOnInit() {
    this.carregarPacientes();
  }

  carregarPacientes() {
    this.clinicaService.listarPacientes().subscribe({
      next: (dados) => this.listaPacientes = dados,
      error: (err) => console.error('Erro ao listar:', err)
    });
  }

  // =============================
  // LÓGICA DA FICHA E EVOLUÇÃO
  // =============================
  abrirFicha(paciente: any) {
    // Criamos uma cópia para edição para não alterar a lista principal antes de salvar
    this.pacienteSelecionado = { ...paciente };
    this.listaEvolucoes = [];

    this.clinicaService.listarEvolucoes(paciente.id).subscribe({
      next: (dados) => this.listaEvolucoes = dados,
      error: (err) => console.error('Erro ao buscar evoluções:', err)
    });

    window.scrollTo({ top: 0, behavior: 'smooth' });
  }

  adicionarEvolucao() {
    if (!this.novaEvolucao.trim()) return;

    // Formata a data para LocalDateTime (YYYY-MM-DDTHH:mm:ss) tirando o 'Z' do final
    const agora = new Date().toISOString().split('.')[0];

    const dados = {
      pacienteId: this.pacienteSelecionado.id,
      texto: this.novaEvolucao,
      dataRegistro: agora
    };

    this.clinicaService.salvarEvolucao(dados).subscribe({
      next: () => {
        this.novaEvolucao = '';
        this.abrirFicha(this.pacienteSelecionado);
      },
      error: (err) => {
        console.error('Erro retornado do Java:', err);
        alert('Erro ao salvar. Verifique se você criou a EvolucaoController no Java.');
      }
    });
  }

  // =============================
  // CADASTRO / ATUALIZAÇÃO
  // =============================
  cadastrarPaciente() {
    // Se tiver alguém selecionado, estamos EDITANDO. Se não, é NOVO.
    const dadosParaSalvar = this.pacienteSelecionado ? this.pacienteSelecionado : this.novoPaciente;

    if (!dadosParaSalvar.nome) return alert('Nome obrigatório');

    const fd = new FormData();
    fd.append('paciente', new Blob([JSON.stringify(dadosParaSalvar)], { type: 'application/json' }));

    if (this.arquivoSelecionado) {
      fd.append('exame', this.arquivoSelecionado);
    }

    this.clinicaService.salvarPaciente(fd).subscribe({
      next: () => {
        alert(this.pacienteSelecionado ? 'Prontuário Atualizado!' : 'Paciente Cadastrado!');
        this.arquivoSelecionado = null;
        if (!this.pacienteSelecionado) {
          this.novoPaciente = this.resetForm();
        } else {
          this.pacienteSelecionado = null; // Fecha a ficha ao salvar a edição
        }
        this.carregarPacientes();
      },
      error: (err) => console.error('Erro ao salvar:', err)
    });
  }

  // =============================
  // FORMATADORES E CÁLCULOS
  // =============================

  // Função para calcular semanas de reabilitação na ficha
  calcularSemanas(dataInicio: string): number {
    if (!dataInicio) return 0;
    const inicio = new Date(dataInicio);
    const hoje = new Date();
    const diferencaMs = hoje.getTime() - inicio.getTime();
    const semanas = Math.floor(diferencaMs / (1000 * 60 * 60 * 24 * 7));
    return semanas >= 0 ? semanas : 0;
  }

  resetForm() {
    return {
      nome: '', cpf: '', telefone: '', peso: '', altura: '',
      condicao: '', plano: 'mensalPosOp',
      dataInicioPlano: new Date().toISOString().split('T')[0], // Sugere data de hoje
      historicoLesao: '', objetivos: '', endereco: '', pagoEsteMes: false
    };
  }

  onFileSelected(event: any) {
    if (event.target.files.length > 0) {
      this.arquivoSelecionado = event.target.files[0];
    }
  }

  // CPF e Telefone agora formatam tanto no cadastro quanto na ficha aberta
  formatarCPF() {
    let alvo = this.pacienteSelecionado ? this.pacienteSelecionado : this.novoPaciente;
    let v = alvo.cpf.replace(/\D/g, '');
    if (v.length > 11) v = v.substring(0, 11);
    v = v.replace(/(\d{3})(\d{3})(\d{3})(\d{2})/, "$1.$2.$3-$4");
    alvo.cpf = v;
  }

  formatarTelefone() {
    let alvo = this.pacienteSelecionado ? this.pacienteSelecionado : this.novoPaciente;
    let v = alvo.telefone.replace(/\D/g, '');
    if (v.length > 11) v = v.substring(0, 11);
    if (v.length > 10)
      v = v.replace(/^(\d{2})(\d{5})(\d{4})/, "($1) $2-$3");
    else if (v.length > 2)
      v = v.replace(/^(\d{2})(\d)/, "($1) $2");
    alvo.telefone = v;
  }
  // Adicione esta função no seu arquivo .ts
  verificarCobranca(dataInicio: string): boolean {
    if (!dataInicio) return false;

    const inicio = new Date(dataInicio);
    const hoje = new Date();

    // Calcula a diferença em dias
    const diffTime = hoje.getTime() - inicio.getTime();
    const diffDays = Math.floor(diffTime / (1000 * 60 * 60 * 24));

    // Pega o dia do ciclo (ex: se passou 25 dias, 55 dias, 85 dias...)
    const diaDoCiclo = diffDays % 30;

    // Se estiver entre o dia 25 e 30 do ciclo mensal, retorna true (alerta)
    return diaDoCiclo >= 25;
  }
}
