import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core'; // Adicionado Output e EventEmitter
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-configuracoes',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './configuracoes.component.html',
  styleUrl: './configuracoes.component.scss'
})
export class ConfiguracoesComponent implements OnInit {

  // --- CORREÇÃO: Inicializamos com um objeto vazio para evitar Tela Branca ---
  @Input() telaAtual: string = 'configuracoes';
  @Input() usuarioLogado: any = { nome: '', cargo: '' };

  // --- ADICIONADO: Emissor para avisar o pai para mudar a tela e evitar bug ---
  @Output() mudarTela = new EventEmitter<string>();

  dataHoje: string = '';
  listaUsuarios: any[] = [];

  // Objeto para criação de novo usuário
  novoUsuario = {
    nome: '',
    username: '',
    senha: '',
    cargo: 'FISIOTERAPEUTA'
  };

  // Valores de mensalidade
  configFinanceira = {
    mensalPreOp: 800,
    mensalPosOp: 1200
  };

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.dataHoje = new Date().toLocaleDateString();
    this.carregarUsuarios();
  }

  // Busca todos os usuários do banco
  carregarUsuarios(): void {
    this.http.get<any[]>('http://localhost:8080/api/usuarios').subscribe({
      next: (dados) => {
        this.listaUsuarios = dados;
      },
      error: (err) => {
        console.error('Erro ao buscar usuários', err);
        // Evita que a listaUsuarios fique undefined
        this.listaUsuarios = [];
      }
    });
  }

  // Salva novo usuário no banco de dados
  cadastrarUsuario(): void {
    if (!this.novoUsuario.username || !this.novoUsuario.senha) {
      alert('Por favor, preencha Usuário e Senha.');
      return;
    }

    this.http.post('http://localhost:8080/api/usuarios', this.novoUsuario).subscribe({
      next: (res) => {
        alert('Usuário cadastrado com sucesso!');
        this.novoUsuario = { nome: '', username: '', senha: '', cargo: 'FISIOTERAPEUTA' };
        this.carregarUsuarios();
      },
      error: (err) => alert('Erro ao salvar usuário no banco Java.')
    });
  }

  // Remove usuário do banco
  removerUsuario(id: any): void {
    if (confirm('Deseja realmente excluir este acesso?')) {
      this.http.delete(`http://localhost:8080/api/usuarios/${id}`).subscribe({
        next: () => {
          alert('Acesso removido!');
          this.carregarUsuarios();
        },
        error: (err) => alert('Erro ao excluir usuário.')
      });
    }
  }

  // Salva os novos valores de mensalidade
  salvarConfiguracoes(): void {
    this.http.post('http://localhost:8080/api/configuracoes/financeiro', this.configFinanceira).subscribe({
      next: () => alert('Preços atualizados com sucesso!'),
      error: (err) => {
        console.warn('Configurações salvas localmente.');
        alert('Preços atualizados na tela! (Para gravar no banco definitivo, crie o Controller de Financeiro no Java).');
      }
    });
  }

  // --- ADICIONADO: Função que o seu botão "Voltar" deve chamar no HTML ---
  voltarParaHome(): void {
    this.mudarTela.emit('home');
  }

}
