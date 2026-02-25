import { Component, OnInit } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterOutlet } from '@angular/router';
import { AvaliacaoComponent } from './avaliacao/avaliacao.component';
import { AgendaComponent } from './agenda/agenda.component';
import { PacientesComponent } from './pacientes/pacientes.component';
import { FinanceiroComponent } from './financeiro/financeiro.component';
import { ConfiguracoesComponent } from './configuracoes/configuracoes.component';
import { ClinicaService } from './services/clinica.service';
import { HttpClient } from '@angular/common/http'; // Importação OK

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterOutlet, AvaliacaoComponent, AgendaComponent, PacientesComponent, FinanceiroComponent, ConfiguracoesComponent],
  providers: [DatePipe],
  templateUrl: './app.html',
  styleUrls: ['./app.scss']
})
export class App implements OnInit {
  // --- Controle de Acesso ---
  logado: boolean = false;
  usuarioLogado: any = {
    nome: 'Dr. Ramon',
    cargo: 'MASTER'
  };

  // --- Navegação ---
  telaAtual: string = 'login';
  dataHoje: string = '';

  // --- Dados do Login ---
  loginData = { username: '', senha: '' };

  // --- Estado da Interface ---
  exibindoResumoAgenda: boolean = false;
  exibindoFicha: boolean = false;
  editandoPaciente: any = null;
  pacienteResumo: any = null;
  pacienteSelecionado: any = null;

  // --- Dados do Sistema (Listas) ---
  listaPacientes: any[] = [];
  alertasVencimento: any[] = [];

  // CORREÇÃO: Adicionado o HttpClient aqui dentro dos parênteses
  constructor(
    public clinicaService: ClinicaService,
    private http: HttpClient
  ) {}

  ngOnInit() {
    this.dataHoje = new Date().toLocaleDateString('pt-BR');
    this.carregarDadosIniciais();
  }

  // Método para buscar os pacientes reais do banco de dados
  carregarDadosIniciais() {
    this.clinicaService.listarPacientes().subscribe({
      next: (dados) => {
        this.listaPacientes = dados;
        console.log('Pacientes carregados no Dashboard:', dados);
      },
      error: (err) => console.error('Erro ao carregar dashboard:', err)
    });
  }

  // --- Métodos de Ação ---
  fazerLogin() {
    // Agora o this.http vai funcionar porque foi declarado no constructor
    this.http.post('http://localhost:8080/api/usuarios/login', this.loginData).subscribe({
      next: (usuario: any) => {
        if (usuario) {
          this.logado = true;
          this.usuarioLogado = usuario;
          this.telaAtual = 'home';
        } else {
          alert('Usuário ou senha inválidos!');
        }
      },
      error: () => alert('Erro ao conectar com o servidor! Verifique se o Java está rodando.')
    });
  }

  logout() {
    this.logado = false;
    this.telaAtual = 'login';
    this.loginData = { username: '', senha: '' };
  }

  mudarTela(novaTela: string) {
    this.telaAtual = novaTela;
    if (novaTela === 'home') {
      this.carregarDadosIniciais();
    }
  }

  abrirFicha(paciente: any) {
    this.pacienteSelecionado = paciente;
    this.exibindoFicha = true;
    this.telaAtual = 'pacientes';
  }

  // --- Lógica de Cobrança e Ciclo de 30 Dias ---

  verificarRenovacaoProxima(dataInicio: string): boolean {
    if (!dataInicio) return false;
    const dias = this.diasParaVencer(dataInicio);
    return dias <= 5 && dias >= 0;
  }

  diasParaVencer(dataInicio: string): number {
    if (!dataInicio) return 0;
    const inicio = new Date(dataInicio + 'T00:00:00');
    const hoje = new Date();
    hoje.setHours(0, 0, 0, 0);
    const diffTime = hoje.getTime() - inicio.getTime();
    const diffDays = Math.floor(diffTime / (1000 * 60 * 60 * 24));
    const diasPassadosNoCiclo = diffDays % 30;
    const restantes = 30 - diasPassadosNoCiclo;
    return restantes === 0 ? 30 : restantes;
  }

  contarPendentes(): number {
    if (!this.listaPacientes) return 0;
    return this.listaPacientes.filter(p =>
      p.pagoEsteMes === false && p.pago_este_mes != 1
    ).length;
  }

  contarVencimentosProximos(): number {
    if (!this.listaPacientes) return 0;
    return this.listaPacientes.filter(p => this.verificarRenovacaoProxima(p.dataInicioPlano)).length;
  }

  alternarPagamento(paciente: any) {
    const novoStatus = !(paciente.pagoEsteMes || paciente.pago_este_mes == 1);
    paciente.pagoEsteMes = novoStatus;
    paciente.pago_este_mes = novoStatus ? 1 : 0;

    if (novoStatus) {
      paciente.dataInicioPlano = new Date().toISOString().split('T')[0];
    }

    const fd = new FormData();
    fd.append('paciente', new Blob([JSON.stringify(paciente)], { type: 'application/json' }));

    this.clinicaService.salvarPaciente(fd).subscribe({
      next: () => {
        this.carregarDadosIniciais();
      },
      error: (err) => {
        console.error("Erro ao salvar pagamento", err);
      }
    });
  }

  abrirResumoAgenda(agendamento: any) {
    const pacienteCompleto = this.listaPacientes.find(p => p.nome === agendamento.title);
    this.pacienteResumo = {
      nome: agendamento.title,
      condicao: agendamento.lesao || (pacienteCompleto ? pacienteCompleto.condicao : 'Não informada'),
      horario: agendamento.start.includes('T') ? agendamento.start.split('T')[1] : agendamento.start,
      id: agendamento.pacienteId || (pacienteCompleto ? pacienteCompleto.id : null),
      telefone: pacienteCompleto ? pacienteCompleto.telefone : 'N/A'
    };
    this.exibindoResumoAgenda = true;
  }
}
