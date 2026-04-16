# Firebase Setup

## Objetivo

Documentar a configuracao local minima do Firebase para desenvolver o MVP do PlusFisio sem versionar configuracoes de ambiente reais no repositório.

## O que fica versionado

- `firebase.json`
- `firestore.rules`
- `firestore.indexes.json`

Esses arquivos definem comportamento esperado do backend e podem permanecer no repositório.

## O que nao fica versionado

- `.firebaserc`
- `composeApp/google-services.json`
- `iosApp/iosApp/GoogleService-Info.plist`

Esses arquivos apontam para um projeto Firebase concreto e devem existir apenas no ambiente local ou no CI seguro.

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
6. adicionar o pacote nativo do Firebase para que `FirebaseCore` esteja disponivel

O app iOS agora protege `FirebaseApp.configure()` quando o plist nao existe, evitando crash em ambientes sem configuracao local.

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

## Observacoes

- Nao publique ids de projeto, app ids, API keys de configuracao cliente ou arquivos de ambiente reais em PRs.
- Se for necessario CI com Firebase real, use secrets do provedor de CI e gere os arquivos durante o pipeline.
- Sem configuracao local do Firebase, o app deve continuar compilando; os fluxos que dependem de backend real ficam indisponiveis ate a configuracao ser adicionada.
