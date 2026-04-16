# PlusFisio

SaaS mobile-first para pequenos estúdios de Pilates e clínicas pequenas de fisioterapia, focado em agenda, confirmação, sessões, presença/falta e cobrança simples.

## Status

- Fase atual: definição de produto e fundação técnica
- Objetivo atual: preparar o MVP para implementação e validação comercial
- Plataforma inicial: Android primeiro, com base KMP preparada para iOS
- Backend: Firebase

## Objetivo do MVP

Entregar um app que permita ao negócio operar o dia a dia com segurança usando o celular, centralizando:

- agenda diária e semanal
- confirmação de atendimento
- controle de pacotes e sessões restantes
- presença, falta e remarcação
- cobrança simples com lembretes

## Público-alvo

O ICP inicial do PlusFisio é:

- estúdios de Pilates e clínicas pequenas de fisioterapia
- negócios com 1 a 3 profissionais
- operação fortemente baseada em celular e WhatsApp
- baixa maturidade administrativa

## Stack

- Kotlin Multiplatform
- Compose Multiplatform
- Android e iOS
- Firebase Authentication
- Cloud Firestore
- Cloud Functions
- Firebase Cloud Messaging
- Crashlytics e Analytics

## Decisões técnicas já tomadas

- app mobile-first
- KMP + Compose Multiplatform como base compartilhada
- Firebase como backend do MVP
- sem web admin no MVP inicial
- WhatsApp via deep link com mensagem pré-preenchida
- arquitetura multi-tenant por `studioId`
- lançamento comercial inicial em Android, seguido de iOS

## MVP

### Escopo operacional

- login e onboarding básico do negócio
- cadastro de clientes
- agenda diária e semanal
- criação, edição, remarcação e cancelamento de agendamentos
- confirmação manual ou via WhatsApp
- cadastro de pacotes de sessões
- controle de sessões restantes
- registro de presença e falta
- cobrança simples
- lembretes de vencimento
- home operacional do dia

### Checklist de entrega

- [ ] cadastrar cliente
- [ ] criar atendimento
- [ ] remarcar atendimento
- [ ] confirmar atendimento
- [ ] abrir WhatsApp com mensagem pronta
- [ ] registrar presença
- [ ] registrar falta
- [ ] consumir sessão do pacote
- [ ] renovar pacote
- [ ] registrar cobrança
- [ ] visualizar pendências do dia na home

### Critérios de sucesso do MVP

- o usuário consegue cadastrar cliente sem treinamento
- o usuário consegue criar e remarcar atendimento em poucos passos
- o usuário consegue registrar presença/falta no fluxo da agenda
- o usuário consegue controlar o saldo de sessões de um cliente
- o usuário consegue registrar e acompanhar cobranças simples
- o usuário consegue abrir o WhatsApp com mensagem pronta a partir de um atendimento

## Arquitetura resumida

- `commonMain`: domínio, casos de uso, repositórios, estados de tela e maior parte da UI
- `androidMain` e `iosMain`: notificações, deep links, permissões e integrações nativas
- camadas lógicas: `presentation`, `domain`, `data`, `platform`
- persistência local leve para sessão, preferências e cache recente
- offline parcial, sem sync complexa no MVP

## Como rodar

### Requisitos

- Android Studio atualizado
- Xcode para build iOS
- JDK 11
- configuração de projeto Firebase

### Setup inicial

1. Clonar o repositório.
2. Abrir o projeto no Android Studio.
3. Configurar Firebase CLI e arquivos do projeto.
4. Rodar o app Android pelo módulo `composeApp`.
5. Abrir `iosApp` no Xcode quando a configuração iOS estiver pronta.

### Observação

Esta seção ainda é um placeholder operacional. O setup detalhado deve ser refinado junto com a integração real do Firebase.

Setup detalhado do Firebase: `docs/firebase-setup.md`.

## Roadmap curto

- Fase 1: MVP operacional mobile
- Fase 2: refinamentos mobile e estabilização iOS
- Fase 3: expansão com web admin
- Fase 4: recursos avançados e integrações

## Documentação

- [Produto](docs/product.md)
- [MVP](docs/mvp.md)
- [Arquitetura](docs/architecture.md)
- [Firestore Model](docs/firestore-model.md)
- [Firebase Setup](docs/firebase-setup.md)
- [Auth Foundation](docs/auth.md)
- [Engineering Workflow](docs/engineering.md)
- [Project Management](docs/project-management.md)

## Auth Foundation Update

The repository now includes the first functional auth foundation for the MVP:

- splash -> session gate -> login/onboarding placeholder/home template
- typed auth contracts in `commonMain`
- Koin wiring for auth and app root ViewModels
- Firebase Auth + Firestore wired as the default provider on Android
- iOS app prepared for Firebase, with the native Apple package still pending in Xcode

## Firestore Foundation Update

The repository now includes the initial Firestore foundation for the MVP:

- canonical tenant model with `users`, `studios`, `members`, `clients`, `appointments`, `packages`, `packageLedger`, and `payments`
- real onboarding bootstrap that creates the first studio and membership
- security rules aligned with tenant isolation and owner/member permissions
- indexes limited to the operational queries most likely to be used in the Spark plan
