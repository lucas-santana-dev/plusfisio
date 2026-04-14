# PlusFisio

## 1. Visão do produto

O **PlusFisio** é um SaaS mobile-first para **pequenos estúdios de Pilates e clínicas pequenas de fisioterapia** que precisam organizar a operação do dia a dia sem depender de planilhas, memória, papel e conversas espalhadas no WhatsApp.

A proposta de valor do produto é simples: **dar controle operacional para negócios pequenos sem exigir estrutura administrativa robusta**. O PlusFisio deve ajudar o negócio a saber, com poucos toques:

- quem será atendido hoje
- quem confirmou ou ainda precisa ser confirmado
- quem faltou ou remarcou
- quantas sessões o cliente ainda tem
- quem está com cobrança pendente ou perto do vencimento

O principal problema que o produto resolve é a **desorganização operacional recorrente**. No nicho inicial, boa parte da rotina ainda acontece em fluxo informal:

- agenda parcialmente na cabeça da recepção ou do profissional
- confirmações feitas manualmente no WhatsApp
- pacotes de sessões controlados em papel ou planilha
- presença e falta registradas de forma inconsistente
- cobranças feitas no improviso

Esse nicho faz sentido porque reúne três características valiosas para um MVP:

- **dor frequente e concreta**: agenda, faltas e cobrança impactam diretamente a receita
- **baixa maturidade de gestão**: há espaço para um produto simples gerar valor rapidamente
- **necessidade de mobilidade**: a operação real acontece no celular, não em mesa administrativa

O PlusFisio não deve ser posicionado como um “sistema de agenda genérico”. A diferenciação em relação a um calendário comum é a combinação de:

- agenda operacional do dia
- controle de pacotes e sessões restantes
- registro de presença, falta e remarcação
- cobrança simples com lembretes
- integração leve com WhatsApp para apoiar a rotina existente

Em outras palavras, o produto deve funcionar como o **sistema operacional da rotina de atendimento** para operações pequenas, e não apenas como uma agenda com horário marcado.

## 2. ICP e usuários

### Perfil do cliente ideal

O ICP inicial do PlusFisio é:

- estúdios de Pilates e clínicas pequenas de fisioterapia
- negócios com **1 a 3 profissionais**
- operação local, simples e enxuta
- forte uso de **celular e WhatsApp**
- pouca formalização administrativa
- dono(a) ainda muito próximo(a) da operação

São negócios que normalmente já sentem dor de organização, mas ainda não querem um sistema pesado, caro ou complexo.

### Personas principais

**1. Dono(a) operador(a)**

- atende clientes e também cuida da gestão
- quer reduzir faltas, controlar sessões e não perder cobrança
- valoriza simplicidade acima de sofisticação

**2. Recepção enxuta / assistente administrativa**

- organiza agenda e confirma atendimentos
- precisa de visão rápida do dia
- executa remarcações e acompanha pendências

**3. Profissional que também opera a agenda**

- atende e ao mesmo tempo marca, remarca e confirma
- precisa registrar presença e consultar histórico operacional com agilidade

### Quem compra

No início, quem compra tende a ser:

- o(a) dono(a) do estúdio ou clínica
- o(a) sócio(a) responsável pela operação
- o profissional autônomo que centraliza a gestão do espaço

### Quem usa no dia a dia

Os usuários diários do MVP serão:

- recepção
- profissionais
- dono(a) quando acumula função operacional

### Quem evitar no início

O PlusFisio deve evitar, no começo:

- clínicas maiores com mais de 4 ou 5 profissionais
- operações multiunidade
- negócios com necessidade de prontuário estruturado robusto
- clínicas com regras complexas de faturamento, convênio ou repasse
- negócios que exigem permissões muito granulares logo no início
- empresas que dependem de web admin completo desde o primeiro dia

Esses perfis aumentam escopo, suporte e complexidade antes de o produto validar valor no mercado inicial.

## 3. Jobs to be done / dores principais

### Principais dores operacionais

As dores principais do nicho inicial são operacionais, repetitivas e diretamente ligadas à receita:

- perder controle da agenda do dia
- esquecer confirmações e gerar faltas evitáveis
- não saber quantas sessões restam em um pacote
- não registrar adequadamente presença, falta e remarcação
- cobrar tarde ou esquecer vencimentos
- depender de várias fontes de informação ao mesmo tempo

### Tarefas críticas do dia a dia

No fluxo real do negócio, as tarefas críticas são:

- visualizar rapidamente a agenda do dia e da semana
- cadastrar novo cliente
- criar ou remarcar um atendimento
- confirmar atendimento manualmente
- abrir o WhatsApp com mensagem pronta
- registrar presença ou falta
- consumir sessão de um pacote
- marcar cobrança como pendente, paga ou vencida
- renovar um pacote de sessões

### Gargalos atuais no WhatsApp / papel / planilha

Hoje, esses negócios normalmente operam com uma combinação frágil de:

- **WhatsApp** para conversar e confirmar
- **papel** para anotar presença e controle de pacote
- **planilha** para alguma organização financeira
- **memória** do dono ou da recepção para fechar lacunas

Isso gera gargalos concretos:

- informação duplicada ou divergente
- baixa rastreabilidade do que já foi feito
- dificuldade de saber o estado real de cada cliente
- remarcações mal controladas
- pouca previsibilidade de receita recorrente

O job principal do produto é permitir que o usuário diga: **“consigo operar meu dia com segurança usando só o app”**.

## 4. Escopo do MVP

O MVP precisa ser **realista, vendável e rápido de entregar**. A prioridade não é construir um sistema completo de clínica, e sim resolver o núcleo operacional com uma UX clara.

### Must have

Funcionalidades que devem entrar no MVP:

- login e sessão do usuário
- onboarding básico do negócio
- cadastro do estúdio/clínica e configuração inicial mínima
- cadastro de clientes/pacientes
- agenda diária e semanal
- criação, edição, remarcação e cancelamento de agendamento
- confirmação manual do atendimento
- atalho para confirmação por WhatsApp via deep link com mensagem pronta
- cadastro de pacotes de sessões
- controle de sessões restantes
- registro de presença
- registro de falta
- remarcação simples
- cobrança simples com status básico
- lembretes de vencimento
- home operacional do profissional/recepção com visão do dia

### Should have

Funcionalidades importantes, mas que podem entrar logo após o núcleo:

- histórico básico do cliente
- timeline operacional simples por cliente
- notificações push para lembretes essenciais
- filtros básicos na agenda
- relatórios simples de presença, faltas e cobranças

### Could have

Funcionalidades úteis, mas não necessárias para vender o primeiro MVP:

- templates editáveis de mensagem de WhatsApp
- tags para clientes
- observações rápidas por atendimento
- exportação simples de dados
- indicadores básicos de desempenho da operação

### Not now

Funcionalidades que **não devem entrar** no MVP:

- prontuário clínico estruturado
- anamnese detalhada
- integração oficial com WhatsApp Business API
- gateway de pagamento, PIX automatizado ou conciliação
- financeiro completo
- repasse para profissionais
- multiunidade
- permissões avançadas por perfil
- painel web admin no MVP
- desktop
- relatórios gerenciais profundos

### Trade-off principal do escopo

O maior trade-off do MVP é abrir mão de profundidade clínica e financeira para ganhar velocidade de validação comercial. Isso é uma escolha correta para o momento porque:

- o problema mais urgente do nicho é operacional
- o valor pode ser percebido rapidamente
- o risco técnico e de produto cai bastante
- o caminho para receita fica mais curto

## 5. Fluxos principais

### 1. Cadastrar cliente

Fluxo esperado:

1. Usuário acessa a área de clientes.
2. Inicia um novo cadastro com dados mínimos.
3. Salva o cliente.
4. Cliente passa a poder ser agendado, associado a pacote e acompanhado no histórico operacional.

Dados mínimos recomendados:

- nome
- telefone
- observação curta opcional

### 2. Criar agendamento

Fluxo esperado:

1. Usuário acessa agenda diária ou semanal.
2. Seleciona dia e horário.
3. Escolhe cliente e profissional.
4. Define tipo/status inicial do agendamento.
5. Salva.

Resultado:

- o atendimento aparece na agenda
- o cliente pode ser confirmado depois
- o agendamento passa a alimentar a home do dia

### 3. Confirmar atendimento

Fluxo esperado:

1. Usuário abre o agendamento.
2. Escolhe confirmar manualmente ou abrir confirmação por WhatsApp.
3. Se usar WhatsApp, o app gera mensagem pronta via deep link.
4. Usuário registra no sistema o status final de confirmação.

Decisão de MVP:

- o envio via WhatsApp será manual, assistido pelo app
- o sistema não depende de API oficial nem tenta automatizar entrega no primeiro momento

### 4. Iniciar e concluir sessão

Fluxo esperado:

1. Na hora do atendimento, usuário acessa a agenda ou a home.
2. Marca presença do cliente.
3. O sistema atualiza o atendimento e consome a sessão quando aplicável.
4. O histórico operacional do cliente é atualizado.

### 5. Registrar falta

Fluxo esperado:

1. Usuário abre o atendimento.
2. Marca como falta.
3. O sistema pode registrar a ausência e aplicar a política configurada para consumo da sessão.

Decisão aberta para implementação:

- a política exata de consumo de sessão por falta ainda precisa ser definida

Recomendação inicial:

- suportar ao menos duas opções futuras: falta consome sessão ou falta não consome sessão
- no primeiro corte de implementação, deixar uma política padrão simples por negócio

### 6. Cobrar cliente

Fluxo esperado:

1. Usuário acessa a área financeira ou o detalhe do cliente.
2. Registra uma cobrança simples vinculada ao cliente e, quando fizer sentido, ao pacote.
3. Define valor, vencimento e status.
4. Atualiza para pago, pendente ou vencido conforme a rotina.

### 7. Renovar pacote

Fluxo esperado:

1. Usuário acessa o cliente.
2. Visualiza saldo de sessões.
3. Cria um novo pacote.
4. O cliente passa a ter novo saldo disponível e nova referência de cobrança.

## 6. Estrutura das telas

O aplicativo deve ser pensado como **mobile-first**, com navegação rasa, foco em ações rápidas e baixa sobrecarga visual.

### Árvore principal de telas

- Autenticação
- Onboarding inicial
- Home
- Agenda
- Clientes
- Financeiro
- Mais / Configurações

### Detalhamento sugerido

#### Autenticação

- Tela de login
- Tela de recuperação de acesso

#### Onboarding inicial

- Boas-vindas
- Cadastro inicial do negócio
- Configuração mínima de profissional e preferências operacionais

#### Home

- Resumo do dia
- Atendimentos de hoje
- Pendências de confirmação
- Cobranças próximas do vencimento
- Ações rápidas

#### Agenda

- Agenda diária
- Agenda semanal
- Detalhe do agendamento
- Criar/editar agendamento

#### Clientes

- Lista de clientes
- Detalhe do cliente
- Cadastro/edição de cliente
- Histórico básico do cliente
- Pacotes do cliente

#### Financeiro

- Lista de cobranças
- Detalhe de cobrança
- Criar/editar cobrança
- Pendências e vencimentos próximos

#### Mais / Configurações

- Perfil do negócio
- Usuários/profissionais
- Preferências operacionais
- Configurações futuras

### Modais e bottom sheets relevantes

- Ações rápidas do agendamento
- Confirmar via WhatsApp
- Registrar presença
- Registrar falta
- Remarcar atendimento
- Renovar pacote
- Atualizar status de cobrança

### Diretriz de navegação

A navegação principal deve priorizar:

- **Home** como centro de operação do dia
- **Agenda** como área de execução
- **Clientes** como base relacional
- **Financeiro** como apoio simples à cobrança

## 7. Arquitetura técnica inicial

A arquitetura deve ser pragmática, enxuta e orientada ao risco real do projeto: você domina Android/Kotlin, mas tem pouca experiência com iOS. Portanto, a estratégia correta é maximizar o compartilhamento em KMP sem forçar abstrações desnecessárias logo no início.

### Direção arquitetural

Adotar uma arquitetura em camadas, com o máximo possível de lógica compartilhada em `commonMain`:

- **presentation**: estados de tela, reducers/event handlers e UI em Compose Multiplatform
- **domain**: casos de uso, regras de negócio e modelos de domínio
- **data**: repositórios, mapeadores, integrações remotas e persistência local
- **platform**: contratos e implementações específicas por plataforma

### Módulos sugeridos

Para o início, a recomendação é **não exagerar na modularização Gradle**. O projeto ainda pode evoluir com um módulo principal e organização interna por pacotes.

Estrutura sugerida:

- `composeApp`
  - `core`
  - `features`
  - `domain`
  - `data`
  - `platform`

Se o projeto crescer com segurança, a evolução natural pode ser:

- `shared-core`
- `shared-data`
- `shared-design-system`
- `feature-*`

Mas isso não precisa existir no primeiro ciclo.

### O que fica compartilhado

Em `commonMain` devem ficar:

- modelos de domínio
- contratos de repositório
- casos de uso
- regras de negócio de agenda, pacotes, presença e cobrança
- estados de tela
- navegação compartilhada
- a maior parte da UI em Compose
- formatação e validação básica

### O que pode ficar específico por plataforma

Em `androidMain` e `iosMain` podem ficar:

- integração de notificações nativas
- abertura de deep links externos
- manipulação de permissões
- detalhes de lifecycle
- pequenas adaptações de integração com SDKs
- bridge específica necessária para bibliotecas que não cubram KMP de forma adequada

### Navegação

Usar uma solução de navegação **simples e estável**, evitando complexidade cedo demais.

Recomendação:

- navegação declarativa por rotas/telas
- pilha simples para fluxos principais
- suporte a modais e bottom sheets para ações rápidas

O foco da navegação deve ser clareza operacional, não sofisticação arquitetural.

### Gerenciamento de estado

Recomendação:

- state holders por tela
- `StateFlow` para estado observável
- `UI state` imutável
- eventos explícitos de interface
- efeitos colaterais isolados

Isso mantém o app previsível, testável e coerente entre Android e iOS.

### Camada de dados

A camada de dados deve seguir este desenho:

- repositórios por agregado principal
- mapeamento entre modelo remoto e domínio
- fonte remota no Firebase
- persistência local leve para sessão, cache e suporte offline parcial

Recomendação prática:

- offline parcial, não offline-first completo no MVP
- cache local de dados recentes e sessão autenticada
- resolução de conflitos simples, evitando sincronização complexa no início

### Integração com Firebase

O Firebase entra como backend principal do MVP, com foco em reduzir tempo de entrega.

Papéis esperados:

- autenticação de usuários
- armazenamento operacional transacional leve
- automações assíncronas
- push notifications
- observabilidade básica de produção

### Autenticação

Recomendação inicial:

- login com **e-mail e senha**

Motivos:

- implementação mais simples
- menor fricção técnica no começo
- mais previsível no KMP

Pode evoluir depois para:

- login por telefone
- magic link
- convite de usuários

### Persistência local

Persistência local deve cobrir:

- sessão autenticada
- preferências simples
- cache de leitura recente
- suporte leve a retomada de contexto

Não é recomendável implementar sincronização offline complexa no MVP.

### Notificações

Notificações no MVP devem ser usadas com parcimônia:

- lembretes do dia
- alertas de cobrança próxima do vencimento
- eventos simples de operação

No início, o objetivo não é construir um motor sofisticado de automação, e sim garantir o essencial.

### Integração com WhatsApp

No MVP, a integração com WhatsApp deve ser feita por **deep link** ou abordagem equivalente.

Fluxo esperado:

- o app monta a mensagem
- abre o WhatsApp
- o usuário envia manualmente
- o status da confirmação é atualizado dentro do app

Trade-off:

- perde automação total
- ganha velocidade de entrega, baixo custo e baixo risco regulatório/técnico

Essa é a decisão correta para o estágio atual do produto.

## 8. Backend e modelo de dados

## Serviços do Firebase recomendados

### Firebase Authentication

Uso no projeto:

- autenticação de usuários
- sessão do app
- vínculo do usuário ao estúdio/clínica

Estratégia inicial:

- e-mail e senha

### Cloud Firestore

Uso no projeto:

- base principal de dados operacionais
- leitura e escrita de clientes, agenda, pacotes, cobranças e usuários

É o núcleo do backend do MVP.

### Cloud Functions

Uso no projeto:

- regras de negócio que não devem viver apenas no cliente
- rotinas de lembrete
- consistência de ações críticas
- pontos de integração e automação futura

Exemplos:

- agendar lembretes de vencimento
- validar atualizações sensíveis de status
- manter consistência em consumo de sessão

### Firebase Cloud Messaging

Uso no projeto:

- envio de notificações push essenciais

### Firebase Crashlytics

Uso no projeto:

- monitoramento de crashes
- ganho de confiabilidade em produção desde cedo

### Firebase Analytics

Uso no projeto:

- medir ativação e adoção do MVP
- entender uso de agenda, confirmação, presença e cobrança

### Firebase Storage

Não é central no MVP, mas pode ser usado depois para:

- logo do negócio
- anexos leves
- assets simples

### Trade-off da escolha Firebase

Vantagens:

- acelera backend do MVP
- reduz custo operacional inicial
- encaixa bem com produto mobile-first
- facilita autenticação, push e observabilidade

Desvantagens:

- exige cuidado com modelagem e consultas no Firestore
- pode ficar mais complexo em regras transacionais sofisticadas
- demanda atenção em multi-tenant e segurança desde cedo

### Entidades principais

Entidades recomendadas para o MVP:

- `Studio`
- `User`
- `Professional`
- `Customer`
- `Appointment`
- `Package`
- `PackageSessionLedger`
- `Charge`
- `NotificationJob` ou estrutura equivalente, se necessário

### Relacionamentos principais

- um `Studio` possui usuários, profissionais, clientes, agendamentos, pacotes e cobranças
- um `Customer` pertence a um `Studio`
- um `Appointment` pertence a um `Studio` e referencia `Customer` e `Professional`
- um `Package` pertence a um `Customer`
- um `PackageSessionLedger` registra consumo, estorno, remarcação ou ajuste
- uma `Charge` pertence a um `Customer` e pode estar vinculada a um `Package`

### Estrutura inicial de coleções/documentos

Estrutura sugerida no Firestore:

- `studios/{studioId}`
- `studios/{studioId}/users/{userId}`
- `studios/{studioId}/professionals/{professionalId}`
- `studios/{studioId}/customers/{customerId}`
- `studios/{studioId}/appointments/{appointmentId}`
- `studios/{studioId}/packages/{packageId}`
- `studios/{studioId}/charges/{chargeId}`

Subcoleções ou estruturas derivadas podem ser usadas quando houver motivo claro, mas o ideal é manter a modelagem simples no início.

### Campos essenciais por entidade

#### Studio

- nome
- tipo de negócio
- telefone principal
- status
- configurações operacionais básicas

#### User

- authUid
- studioId
- nome
- papel
- status

#### Professional

- nome
- ativo
- cor ou identificador visual opcional

#### Customer

- nome
- telefone
- observações simples
- ativo
- timestamps principais

#### Appointment

- customerId
- professionalId
- data/hora
- duração
- status
- origem da marcação
- indicador de confirmação

#### Package

- customerId
- nome/plano
- total de sessões
- sessões consumidas
- sessões restantes
- validade opcional
- status

#### PackageSessionLedger

- packageId
- appointmentId opcional
- tipo de movimento
- quantidade
- data
- observação opcional

#### Charge

- customerId
- packageId opcional
- valor
- vencimento
- status
- forma combinada opcional
- observação opcional

### Regras de acesso

O produto deve nascer como **multi-tenant por estúdio/clínica**.

Regras essenciais:

- usuário só acessa dados do próprio `studioId`
- `owner/admin` tem acesso completo aos dados do estúdio
- `professional` tem acesso operacional, com restrições definidas conforme necessidade real

Para o MVP, a recomendação é manter apenas dois grandes níveis:

- `owner_admin`
- `professional`

Evitar granularidade maior no primeiro momento.

### Permissões por tipo de usuário

**Owner/Admin**

- gerencia negócio
- visualiza todos os clientes
- gerencia agenda, pacotes e cobranças
- configura profissionais e preferências

**Professional**

- visualiza e opera agenda conforme escopo definido
- registra presença, falta e conclusão
- acessa dados operacionais necessários do cliente

Decisão aberta:

- o nível exato de restrição por profissional ainda pode ser calibrado depois do primeiro piloto

### Pontos de atenção para evolução futura

- preparar dados para futura camada web admin
- evitar acoplamento excessivo da UI ao formato bruto do Firestore
- tratar agenda e consultas por período com índices adequados
- preservar consistência de pacotes e saldo de sessões
- modelar status de cobrança de forma simples, porém extensível

## 9. Estratégia de UX

O produto deve ser desenhado para a realidade de telas pequenas e uso recorrente ao longo do dia.

### Princípios de UX

- **home como centro de priorização do dia**
- baixo excesso de informação por tela
- ações rápidas evidentes
- leitura imediata de status
- poucos campos por formulário
- foco em operação real, não em dashboard decorativo

### Home do produto

A home deve responder rapidamente:

- o que tenho hoje
- o que ainda precisa de confirmação
- quem está pendente de cobrança
- o que exige ação agora

Seções recomendadas:

- atendimentos de hoje
- pendências de confirmação
- próximas cobranças
- ações rápidas

### Seções expansíveis e recolhíveis

Essa abordagem faz sentido porque:

- reduz densidade visual
- ajuda em telas pequenas
- permite foco no que é prioritário agora

### Operação rápida

O design deve privilegiar:

- toques curtos
- listas fáceis de escanear
- status com leitura visual clara
- bottom sheets para decisões rápidas

### O que evitar na UX inicial

- telas com excesso de métricas
- cadastros longos
- navegação profunda
- excesso de filtros e estados visuais
- linguagem genérica de software corporativo

## 10. Roadmap em fases

### Fase 1: MVP

Objetivo:

- entregar o núcleo operacional vendável

Foco:

- autenticação
- onboarding do negócio
- clientes
- agenda
- confirmação manual/WhatsApp
- pacotes
- presença/falta/remarcação
- cobrança simples
- home operacional

Estratégia de plataforma:

- Android primeiro
- arquitetura já preparada para iOS

### Fase 2: melhorias mobile

Objetivo:

- reduzir fricção e melhorar retenção

Foco:

- refinamento de UX
- filtros melhores
- notificações mais úteis
- relatórios simples
- histórico de cliente mais rico
- estabilização e entrega de iOS

### Fase 3: expansão com web admin

Objetivo:

- oferecer camada de backoffice sem sacrificar a experiência mobile

Foco:

- painel web administrativo
- gestão mais confortável de clientes, cobranças e relatórios
- continuidade da mesma modelagem multi-tenant

### Fase 4: recursos mais avançados

Objetivo:

- aumentar ticket, retenção e profundidade operacional

Foco:

- permissões avançadas
- automações mais fortes
- integração de pagamento
- integração mais profunda com comunicação
- recursos clínicos ou financeiros adicionais, se o mercado justificar

## 11. Riscos e decisões abertas

### Riscos técnicos

- curva de aprendizado em iOS pode atrasar paridade entre plataformas
- algumas integrações Firebase em KMP podem exigir escolha cuidadosa de biblioteca e bridge
- notificações e comportamento em background variam bastante entre Android e iOS
- agenda + remarcação + pacote + cobrança parece simples, mas esconde regras de negócio delicadas

### Riscos de produto

- escopo inflar cedo demais para prontuário ou ERP de clínica
- ICP ficar amplo demais e enfraquecer posicionamento
- cobrança simples não gerar valor percebido se a UX não for muito clara

### Riscos de UX

- home virar painel carregado
- fluxo de agenda ficar lento para uso recorrente
- excesso de estados tornar presença/falta/remarcação confusos

### Riscos de adoção comercial

- cliente pequeno tem baixa tolerância a onboarding demorado
- valor precisa ser percebido muito rápido
- se o produto exigir mudança radical de rotina, a adesão cai

### Principais decisões que ainda precisam ser tomadas

- política exata de consumo de sessão em caso de falta
- nível de permissão entre dono/admin e profissional
- formato final dos lembretes e notificações
- estratégia exata de suporte offline parcial
- profundidade do financeiro após o MVP

## 12. Plano de execução

O plano de execução precisa priorizar validação rápida, sem comprometer a base técnica.

### Ordem recomendada de desenvolvimento

1. Estruturar domínio e modelo de dados principais.
2. Implementar autenticação e contexto de `studioId`.
3. Construir onboarding básico do negócio.
4. Entregar cadastro de clientes.
5. Entregar agenda diária/semanal com criação e remarcação.
6. Adicionar confirmação manual e atalho para WhatsApp.
7. Implementar pacotes e saldo de sessões.
8. Implementar presença, falta e remarcação operacional.
9. Implementar cobrança simples e lembretes.
10. Construir home operacional do dia.
11. Instrumentar Crashlytics e Analytics.
12. Validar piloto Android.
13. Evoluir iOS com base compartilhada já estabilizada.

### Milestones sugeridos

**M1. Fundação técnica**

- base KMP organizada
- autenticação
- contexto de tenant
- onboarding inicial

**M2. Operação básica**

- clientes
- agenda utilizável
- criação e remarcação de atendimentos

**M3. Core de valor**

- confirmação
- pacotes
- presença/falta
- saldo de sessões

**M4. Fechamento comercial do MVP**

- cobrança simples
- lembretes
- home operacional
- métricas básicas e observabilidade

**M5. Pós-validação**

- refinamentos de UX
- iOS
- pequenas melhorias orientadas por uso real

### Dependências principais

- definição final do modelo de dados
- escolha da stack exata de integração Firebase em KMP
- decisão operacional sobre política de faltas
- definição mínima de papéis de usuário

### O que construir primeiro para validar rápido

Primeiro, validar o eixo central do produto:

- agenda
- confirmação
- pacotes/sessões
- presença/falta
- cobrança simples

Se esse núcleo gerar uso recorrente, o produto tem base para monetização.

### O que pode ser postergado

- painel web
- automações complexas
- integrações de pagamento
- permissões avançadas
- prontuário
- relatórios mais ricos

## 13. Recomendação final

A direção mais inteligente para o PlusFisio agora é construir um **produto operacional extremamente simples, mobile-first e focado no dia a dia** de estúdios e clínicas pequenas.

O foco do MVP deve ser:

- agenda do dia
- confirmação de atendimento
- pacotes e sessões restantes
- presença, falta e remarcação
- cobrança simples com lembretes

Esse recorte é o melhor equilíbrio entre:

- valor percebido
- velocidade de entrega
- chance real de lançamento
- potencial de gerar receita ainda este ano

O que deve ser evitado no início:

- tentar resolver todo o universo da clínica
- entrar cedo em prontuário
- automatizar WhatsApp de forma pesada
- construir financeiro completo
- criar web admin antes de validar uso recorrente do mobile

Se o PlusFisio conseguir se tornar o app que o dono ou a recepção abre várias vezes por dia para operar agenda, sessões e cobrança com segurança, o produto já terá encontrado um ponto forte de valor real no mercado.
