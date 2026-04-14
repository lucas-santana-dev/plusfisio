# PlusFisio - Arquitetura

## Visão técnica inicial

A arquitetura deve ser pragmática, enxuta e orientada à velocidade de entrega. O projeto nasce com **Kotlin Multiplatform + Compose Multiplatform** para compartilhar o máximo possível entre Android e iOS, sem complicar desnecessariamente a base.

Como o foco inicial de execução é Android, a estratégia correta é:

- compartilhar domínio, dados e maior parte da UI
- isolar diferenças de plataforma em pontos pequenos e claros
- evitar modularização excessiva no primeiro ciclo

## Arquitetura de aplicação

### Camadas sugeridas

- `presentation`
- `domain`
- `data`
- `platform`

### Responsabilidade de cada camada

**presentation**

- telas em Compose Multiplatform
- estados de tela
- tratamento de eventos
- navegação

**domain**

- casos de uso
- modelos de domínio
- regras de negócio de agenda, pacotes, presença, falta e cobrança

**data**

- repositórios
- mapeadores
- integração com Firebase
- persistência local

**platform**

- notificações nativas
- deep links externos
- permissões
- detalhes específicos de Android e iOS

## Organização sugerida no projeto

No início, a recomendação é manter o projeto simples, usando o módulo `composeApp` com separação interna por pacotes:

- `core`
- `features`
- `domain`
- `data`
- `platform`

Essa abordagem reduz sobrecarga arquitetural e acelera a implementação inicial.

## O que fica compartilhado

Em `commonMain`:

- modelos de domínio
- contratos de repositório
- casos de uso
- regras de negócio
- estados de tela
- maior parte da UI
- navegação compartilhada
- validações e formatações básicas

## O que pode ficar específico por plataforma

Em `androidMain` e `iosMain`:

- integração de notificações
- abertura de WhatsApp via deep link
- permissões e lifecycle
- bridges específicas para SDKs ou bibliotecas

## Navegação

Direção recomendada:

- navegação declarativa por telas/rotas
- pilha simples
- suporte a modais e bottom sheets

O objetivo da navegação é clareza operacional e rapidez de uso.

## Gerenciamento de estado

Recomendação:

- state holders por tela
- `StateFlow`
- `UI state` imutável
- eventos explícitos
- efeitos colaterais isolados

Essa abordagem favorece previsibilidade, teste e portabilidade entre plataformas.

## Camada de dados

Desenho recomendado:

- repositórios por agregado principal
- modelos remotos separados de modelos de domínio
- mapeamento explícito
- persistência local leve
- cache e sessão autenticada

### Estratégia offline

No MVP, a recomendação é:

- offline parcial
- sem sync complexa
- foco em cache, sessão e retomada de contexto

## Firebase no projeto

## Serviços recomendados

### Firebase Authentication

Uso:

- autenticação do usuário
- gestão de sessão
- vínculo com o tenant

Estratégia inicial:

- e-mail e senha

### Cloud Firestore

Uso:

- base principal de dados operacionais

### Cloud Functions

Uso:

- automações leves
- consistência de regras críticas
- lembretes e validações sensíveis

### Firebase Cloud Messaging

Uso:

- notificações push essenciais

### Crashlytics

Uso:

- monitoramento de crashes

### Analytics

Uso:

- medição de ativação e uso do MVP

### Storage

Uso futuro, se necessário:

- logo do negócio
- anexos leves

## Modelo de dados inicial

### Entidades principais

- `Studio`
- `User`
- `Professional`
- `Customer`
- `Appointment`
- `Package`
- `PackageSessionLedger`
- `Charge`

### Relacionamentos

- `Studio` agrega usuários, profissionais, clientes, agendamentos, pacotes e cobranças
- `Customer` pertence a um `Studio`
- `Appointment` referencia `Customer` e `Professional`
- `Package` pertence a um `Customer`
- `PackageSessionLedger` registra consumo e ajustes
- `Charge` pertence a um `Customer` e pode referenciar um `Package`

### Estrutura inicial de coleções

- `studios/{studioId}`
- `studios/{studioId}/users/{userId}`
- `studios/{studioId}/professionals/{professionalId}`
- `studios/{studioId}/customers/{customerId}`
- `studios/{studioId}/appointments/{appointmentId}`
- `studios/{studioId}/packages/{packageId}`
- `studios/{studioId}/charges/{chargeId}`

## Regras de acesso

O produto deve nascer como multi-tenant por `studioId`.

Regras essenciais:

- usuário acessa apenas dados do próprio estúdio
- `owner_admin` tem acesso completo
- `professional` tem acesso operacional

No MVP, não vale a pena criar níveis muito granulares.

## Notificações

No MVP, usar notificações de forma contida:

- lembretes do dia
- cobranças próximas do vencimento
- alertas simples de operação

## WhatsApp

Integração inicial via deep link:

- montar mensagem no app
- abrir o WhatsApp
- usuário envia manualmente
- status é atualizado no app

Essa abordagem reduz risco, custo e dependência externa no começo.

## Pontos de atenção

- curva de aprendizado em iOS
- escolha da stack Firebase em KMP
- diferenças de background e notificações entre Android e iOS
- cuidado com consultas por período no Firestore
- consistência de saldo de pacote e consumo de sessão

## Decisões técnicas já tomadas

- KMP + Compose Multiplatform
- Firebase como backend principal
- mobile-first
- sem web no MVP
- WhatsApp via deep link
- multi-tenant por `studioId`
- Android primeiro, iOS em seguida
