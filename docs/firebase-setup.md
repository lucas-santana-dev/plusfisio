# Firebase Setup

## Objetivo

Documentar a configuracao local minima do Firebase para desenvolver o MVP do PlusFisio sem versionar configuracoes de ambiente reais no repositorio.

## O que fica versionado

- `firebase.json`
- `firestore.rules`
- `firestore.indexes.json`

Esses arquivos definem comportamento esperado do backend e podem permanecer no repositorio.

## O que nao fica versionado

- `.firebaserc`
- `composeApp/google-services.json`
- `iosApp/iosApp/GoogleService-Info.plist`

Esses arquivos apontam para um projeto Firebase concreto e devem existir apenas no ambiente local ou no CI seguro.

## Integracao iOS no repositorio

O projeto iOS referencia o Firebase Apple SDK diretamente no `iosApp.xcodeproj` via Swift Package Manager.

Produtos atualmente vinculados ao target:

- `FirebaseCore`
- `FirebaseAuth`
- `FirebaseFirestore`

O arquivo `GoogleService-Info.plist` continua local e fora do Git.

## Configuracao local recomendada

### Firebase CLI

1. autenticar localmente com `firebase login`
2. selecionar o projeto correto com `firebase use --add`
3. manter o `.firebaserc` gerado apenas na maquina local

### Android

1. criar ou selecionar um app Android no projeto Firebase
2. usar o package name `br.com.plusapps.plusfisio`
3. baixar `google-services.json`
4. salvar o arquivo em `composeApp/google-services.json`

O arquivo esta no `.gitignore` e o plugin `com.google.gms.google-services` so e aplicado quando ele existe localmente.

### iOS

1. criar ou selecionar um app iOS no projeto Firebase
2. usar o bundle id definido em `iosApp/Configuration/Config.xcconfig`
3. baixar `GoogleService-Info.plist`
4. adicionar o arquivo em `iosApp/iosApp/GoogleService-Info.plist`
5. incluir o plist no target do app no Xcode
6. criar `iosApp/Configuration/Config.local.xcconfig` com o `TEAM_ID` local
7. abrir `iosApp/iosApp.xcodeproj` no Xcode para o SwiftPM resolver os pacotes

O app iOS protege `FirebaseApp.configure()` quando o plist nao existe, evitando crash em ambientes sem configuracao local.

Para o passo a passo completo no Mac, ver `docs/ios-setup.md`.

## Firestore e multi-tenant

O modelo canonico do MVP continua sendo:

- `users/{uid}`
- `studios/{studioId}`
- `studios/{studioId}/members/{uid}`
- `studios/{studioId}/clients/{clientId}`
- `studios/{studioId}/appointments/{appointmentId}`
- `studios/{studioId}/packages/{packageId}`
- `studios/{studioId}/packageLedger/{entryId}`
- `studios/{studioId}/payments/{paymentId}`

As regras devem preservar isolamento por `studioId` e impedir que um usuario manipule tenant sem membership valida.

## Comandos uteis

### Selecionar projeto local

```powershell
firebase use --add
```

### Validar regras

```powershell
firebase deploy --only firestore:rules
```

### Validar indices

```powershell
firebase deploy --only firestore:indexes
```

### Subir emuladores locais

```powershell
firebase emulators:start
```

### Listar projetos disponiveis

```powershell
firebase projects:list
```

## Proximos passos recomendados

1. Criar os apps Android e iOS no console do Firebase.
2. Baixar `google-services.json` e `GoogleService-Info.plist`.
3. Habilitar `Email/Password` no Firebase Authentication.
4. Criar os primeiros usuarios de teste no console do Firebase Authentication.
5. Definir a modelagem inicial do Firestore dentro de `studios/{studioId}`.
6. Implementar o onboarding que cria `studios/{studioId}` e `studios/{studioId}/members/{uid}`.

## Pontos de atencao

- Nao publique ids de projeto, app ids, API keys de configuracao cliente ou arquivos de ambiente reais em PRs.
- Os arquivos `composeApp/google-services.json` e `iosApp/iosApp/GoogleService-Info.plist` precisam existir localmente e estao no `.gitignore`.
- O provider `Email/Password` ainda precisa estar habilitado no console do Firebase Authentication.
- O projeto iOS ja inclui o Firebase Apple SDK via Swift Package Manager; o passo manual remanescente no Mac e apenas fornecer `GoogleService-Info.plist` local e configurar assinatura.
- Se for necessario CI com Firebase real, use secrets do provedor de CI e gere os arquivos durante o pipeline.
- Sem configuracao local do Firebase, o app deve continuar compilando; os fluxos que dependem de backend real ficam indisponiveis ate a configuracao ser adicionada.
- Se o onboarding inicial do estudio ainda nao estiver definido, o usuario autenticado continuara caindo no fluxo de onboarding com `studioId = null`.
- As regras atuais bloqueiam a autoassociacao do usuario a um tenant para preservar o isolamento multi-tenant ate a entrega do onboarding real.

## Quando expandir a configuracao

Adicionar `Cloud Functions` quando houver necessidade clara de:

- lembretes agendados
- regras centrais de negocio sensiveis
- automacoes de cobranca
- sincronizacao de claims ou membership

Adicionar `Storage` quando houver necessidade real de:

- foto de perfil
- anexos simples
- arquivos de suporte operacional
