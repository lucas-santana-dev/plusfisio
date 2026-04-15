# iOS Setup

## Objetivo

Documentar o setup real do app iOS do PlusFisio para abrir, compilar e rodar no Xcode com o minimo de passos manuais, preservando o fluxo atual com `:composeApp:embedAndSignAppleFrameworkForXcode`.

## Estado atual do projeto

- o modulo compartilhado gera framework para `iosArm64`, `iosSimulatorArm64` e `iosX64`
- o projeto iOS usa `embedAndSignAppleFrameworkForXcode` no build do Xcode
- o Firebase Apple SDK esta integrado ao `iosApp.xcodeproj` via Swift Package Manager
- o deployment target do app iOS foi reduzido para `13.0`
- a assinatura local usa override por `.xcconfig`, sem exigir edicao de arquivo rastreado

## Pre-requisitos no Mac

- Xcode instalado
- JDK 17 disponivel para o Gradle
- conta Apple configurada no Xcode se for rodar em dispositivo fisico
- arquivos locais do Firebase para o projeto `plusfisio-a57f5`

## Arquivos locais obrigatorios

### Firebase

O arquivo abaixo deve existir localmente e continua fora do versionamento:

- `iosApp/iosApp/GoogleService-Info.plist`

Use o app iOS do projeto Firebase com bundle id `br.com.plusapps.plusfisio.ios`.

### Assinatura

O repositorio versiona:

- `iosApp/Configuration/Config.xcconfig`
- `iosApp/Configuration/Config.local.xcconfig.example`

Cada maquina deve criar o override local ignorado pelo Git:

- `iosApp/Configuration/Config.local.xcconfig`

Conteudo minimo:

```xcconfig
TEAM_ID=SEU_TEAM_ID
```

O arquivo base usa `#include? "Config.local.xcconfig"`, entao o override local entra sem alterar arquivo rastreado.

## Como abrir e rodar

1. Garanta que o JDK 17 esteja ativo para o Gradle no terminal e no Xcode.
2. Coloque `GoogleService-Info.plist` em `iosApp/iosApp/`.
3. Crie `iosApp/Configuration/Config.local.xcconfig` a partir do exemplo e preencha `TEAM_ID`.
4. Abra `iosApp/iosApp.xcodeproj` no Xcode.
5. Aguarde o Xcode resolver o pacote SwiftPM do Firebase na primeira abertura.
6. Escolha um simulador ou dispositivo.
7. Rode o target `iosApp`.

Durante o build, o Xcode executa:

```sh
./gradlew :composeApp:embedAndSignAppleFrameworkForXcode
```

Isso continua sendo a integracao oficial entre Xcode e o framework KMP compartilhado.

## Validacao por arquitetura

### Mac Apple Silicon

- simulador: usa `iosSimulatorArm64`
- dispositivo fisico: usa `iosArm64`

### Mac Intel

- simulador: usa `iosX64`
- dispositivo fisico: continua usando `iosArm64`

O projeto nao fixa mais `ARCHS = arm64` no target iOS, para nao bloquear simulador Intel.

## Firebase via Swift Package Manager

O projeto iOS referencia o pacote `https://github.com/firebase/firebase-ios-sdk` com versao exata `11.15.0`.

Essa versao foi escolhida para manter o deployment target do app em `iOS 13.0`. Versoes mais novas do Firebase Apple SDK elevam o piso de iOS e quebrariam esse objetivo.

Produtos vinculados ao target `iosApp`:

- `FirebaseCore`
- `FirebaseAuth`
- `FirebaseFirestore`

## Problemas esperados de setup

- sem `GoogleService-Info.plist`, o app nao inicializa o Firebase corretamente
- sem `TEAM_ID`, o Xcode nao consegue assinar para rodar em simulador configurado para assinatura automatica ou em dispositivo
- se o Xcode ainda nao tiver resolvido o SwiftPM, o primeiro build pode falhar ate baixar as dependencias

## Validacao registrada nesta task

- validado por inspecao de configuracao:
  - target KMP com `iosX64`, `iosSimulatorArm64` e `iosArm64`
  - script `embedAndSignAppleFrameworkForXcode` preservado no `iosApp.xcodeproj`
  - Firebase Apple SDK configurado via Swift Package Manager no projeto iOS
  - assinatura local via `.xcconfig` com override ignorado pelo Git
  - `GoogleService-Info.plist` mantido fora do versionamento
- nao validado neste ambiente:
  - abertura real do projeto no Xcode
  - execucao em simulador Apple Silicon
  - execucao em simulador Intel
  - execucao em dispositivo fisico

Essas validacoes ficaram pendentes porque o ambiente atual nao possui macOS, Xcode nem simuladores iOS.
