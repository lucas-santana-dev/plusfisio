# iOS Setup

## Objetivo

Documentar o setup real do app iOS do PlusFisio para abrir, compilar e rodar no Xcode com o minimo de passos manuais, preservando o fluxo atual com `:composeApp:embedAndSignAppleFrameworkForXcode`.

## Estado atual do projeto

- o modulo compartilhado gera framework para `iosArm64`, `iosSimulatorArm64` e `iosX64`
- o projeto iOS usa `embedAndSignAppleFrameworkForXcode` no build do Xcode
- o Firebase Apple SDK esta integrado ao `iosApp.xcodeproj` via Swift Package Manager
- o deployment target do app iOS esta em `14.0`
- a assinatura local usa override por `.xcconfig`, sem exigir edicao de arquivo rastreado

## Pre-requisitos no Mac

- Xcode instalado
- JDK do Android Studio disponivel para o Gradle
- conta Apple configurada no Xcode se for rodar em dispositivo fisico
- arquivos locais do Firebase para o projeto `plusfisio-a57f5`
- Android SDK configurado no terminal

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

1. Garanta que o JDK do Android Studio esteja ativo para o Gradle no terminal e no Xcode.
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

## Toolchain validada nesta maquina

- macOS `15.5`
- Xcode `16.2`
- Gradle wrapper `8.14.3`
- `JAVA_HOME=/Applications/Android Studio.app/Contents/jbr/Contents/Home`

O projeto continua compilando com target Java 17 em Android, mas o runtime do Gradle pode usar JDK 21 sem conflito.

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

O app usa o lifecycle moderno do SwiftUI (`App`, `Scene` e `@UIApplicationDelegateAdaptor`), entao o deployment target efetivo precisa ser `iOS 14.0` ou maior.

Produtos vinculados ao target `iosApp`:

- `FirebaseCore`
- `FirebaseAuth`
- `FirebaseFirestore`

## Problemas esperados de setup

- sem `GoogleService-Info.plist`, o app nao inicializa o Firebase corretamente
- sem `TEAM_ID`, o Xcode nao consegue assinar para rodar em simulador configurado para assinatura automatica ou em dispositivo
- se o Xcode ainda nao tiver resolvido o SwiftPM, o primeiro build pode falhar ate baixar as dependencias

## Validacao registrada nesta task

- validado nesta maquina:
  - target KMP com `iosX64`, `iosSimulatorArm64` e `iosArm64`
  - script `embedAndSignAppleFrameworkForXcode` preservado no `iosApp.xcodeproj`
  - Firebase Apple SDK configurado via Swift Package Manager no projeto iOS
  - resolucao real de pacotes SwiftPM do Firebase no Xcode
  - `xcodebuild -project iosApp/iosApp.xcodeproj -list`
  - `xcrun simctl list`
  - build do framework compartilhado KMP para simulador iniciado com sucesso no Gradle
  - build do projeto iOS iniciado com sucesso no Xcode para simulador local
- ainda depende de arquivos locais para validacao funcional completa:
  - `iosApp/iosApp/GoogleService-Info.plist`
  - `iosApp/Configuration/Config.local.xcconfig` com `TEAM_ID` se houver execucao com assinatura local
