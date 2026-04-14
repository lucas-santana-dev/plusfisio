# PlusFisio - MVP

## Escopo do MVP

O MVP precisa ser realista, vendável e rápido de entregar. O objetivo é resolver o núcleo operacional com uma UX clara.

### Must have

- login e sessão do usuário
- onboarding básico do negócio
- cadastro do estúdio/clínica
- cadastro de clientes/pacientes
- agenda diária e semanal
- criação, edição, remarcação e cancelamento de agendamento
- confirmação manual do atendimento
- atalho para confirmação por WhatsApp via deep link
- cadastro de pacotes de sessões
- controle de sessões restantes
- registro de presença
- registro de falta
- remarcação simples
- cobrança simples com status básico
- lembretes de vencimento
- home operacional com visão do dia

### Should have

- histórico básico do cliente
- timeline operacional simples
- notificações push essenciais
- filtros básicos na agenda
- relatórios simples

### Could have

- templates editáveis de mensagem
- tags para clientes
- observações rápidas por atendimento
- exportação simples
- indicadores básicos de operação

### Not now

- prontuário clínico estruturado
- integração oficial com WhatsApp Business API
- gateway de pagamento
- financeiro completo
- multiunidade
- permissões avançadas
- painel web admin
- desktop

## Fluxos principais

### Cadastrar cliente

1. Usuário acessa clientes.
2. Inicia novo cadastro.
3. Preenche nome, telefone e observação opcional.
4. Salva.

### Criar agendamento

1. Usuário acessa agenda.
2. Seleciona dia e horário.
3. Escolhe cliente e profissional.
4. Salva.

### Confirmar atendimento

1. Usuário abre o agendamento.
2. Escolhe confirmar manualmente ou abrir o WhatsApp.
3. Se usar WhatsApp, o app monta a mensagem e abre o deep link.
4. Usuário atualiza o status da confirmação no sistema.

### Iniciar e concluir sessão

1. Usuário acessa agenda ou home.
2. Marca presença.
3. Sistema atualiza o atendimento.
4. Quando aplicável, consome sessão do pacote.

### Registrar falta

1. Usuário abre o atendimento.
2. Marca falta.
3. Sistema registra a ausência e aplica a política de consumo configurada.

### Cobrar cliente

1. Usuário acessa financeiro ou detalhe do cliente.
2. Registra cobrança simples.
3. Define valor, vencimento e status.
4. Atualiza para pago, pendente ou vencido conforme a rotina.

### Renovar pacote

1. Usuário acessa o cliente.
2. Visualiza saldo.
3. Cria novo pacote.
4. Sistema atualiza o saldo disponível.

## Estrutura das telas

### Navegação principal

- Autenticação
- Onboarding inicial
- Home
- Agenda
- Clientes
- Financeiro
- Mais / Configurações

### Subtelas e detalhes

#### Home

- resumo do dia
- atendimentos de hoje
- pendências de confirmação
- cobranças próximas
- ações rápidas

#### Agenda

- agenda diária
- agenda semanal
- detalhe do agendamento
- criar/editar agendamento

#### Clientes

- lista de clientes
- detalhe do cliente
- cadastro/edição
- histórico básico
- pacotes do cliente

#### Financeiro

- lista de cobranças
- detalhe de cobrança
- criar/editar cobrança

### Modais e bottom sheets

- ações rápidas do agendamento
- confirmar via WhatsApp
- registrar presença
- registrar falta
- remarcar atendimento
- renovar pacote
- atualizar status de cobrança

## Checklist operacional de entrega

- [ ] login funcional
- [ ] onboarding do negócio
- [ ] cadastro de cliente
- [ ] agenda diária
- [ ] agenda semanal
- [ ] criação de agendamento
- [ ] remarcação
- [ ] confirmação manual
- [ ] deep link para WhatsApp
- [ ] pacote com saldo de sessões
- [ ] presença
- [ ] falta
- [ ] cobrança simples
- [ ] lembrete de vencimento
- [ ] home operacional

## Critérios de sucesso do MVP

- cadastrar cliente sem treinamento
- criar e remarcar atendimento em poucos passos
- registrar presença/falta no fluxo da agenda
- controlar pacote e saldo de sessões
- registrar cobrança e acompanhar pendências
- abrir WhatsApp com mensagem pronta a partir do atendimento

## Plano de execução

### Ordem recomendada

1. Estruturar domínio e modelo de dados.
2. Implementar autenticação e contexto de tenant.
3. Construir onboarding básico.
4. Entregar clientes.
5. Entregar agenda.
6. Adicionar confirmação manual e WhatsApp.
7. Adicionar pacotes e saldo de sessões.
8. Adicionar presença/falta.
9. Adicionar cobrança simples e lembretes.
10. Construir home operacional.

### Milestones

**M1. Fundação técnica**

- autenticação
- tenant
- onboarding

**M2. Operação básica**

- clientes
- agenda
- remarcação

**M3. Core de valor**

- confirmação
- pacotes
- presença/falta

**M4. Fechamento do MVP**

- cobrança simples
- lembretes
- home operacional

## Decisões abertas

- política de consumo de sessão em caso de falta
- nível exato de permissão entre admin e profissional
- profundidade das notificações no primeiro corte
- nível de suporte offline parcial
