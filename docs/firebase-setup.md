# Firebase Setup

## Objetivo

Documentar a configuracao inicial do Firebase para o MVP do PlusFisio, com foco em simplicidade operacional, multi-tenant por `studioId` e seguranca suficiente para o inicio do desenvolvimento.

## Projeto vinculado

- Firebase project display name: `PlusFisio`
- Firebase project id: `plusfisio-a57f5`

O repositorio esta vinculado a esse projeto por meio do arquivo `.firebaserc`.

## Arquivos criados

- `.firebaserc`
- `firebase.json`
- `firestore.rules`
- `firestore.indexes.json`

## Apps criados no Firebase

### Android

- display name: `PlusFisio-Android`
- package name: `br.com.plusapps.plusfisio`
- Firebase app id: `1:609787046500:android:916e357e768bc112444d71`
- config local: `composeApp/google-services.json`

### iOS

- display name: `PlusFisio-iOS`
- bundle id: `br.com.plusapps.plusfisio.ios`
- Firebase app id: `1:609787046500:ios:b676dccd1d679e5c444d71`
- config local: `iosApp/iosApp/GoogleService-Info.plist`

## Ajuste feito no projeto iOS

O arquivo `iosApp/Configuration/Config.xcconfig` foi ajustado para usar um bundle identifier fixo:

- `br.com.plusapps.plusfisio.ios`

Isso foi necessario porque o valor anterior dependia de `$(TEAM_ID)`, o que impede o cadastro consistente do app no Firebase.

## O que foi configurado

### Firebase CLI

O CLI esta autenticado localmente e o projeto padrao do repositorio foi configurado como `plusfisio-a57f5`.

### Firestore

O arquivo `firebase.json` aponta para:

- regras em `firestore.rules`
- indices em `firestore.indexes.json`

No momento, o arquivo de indices esta vazio. Isso e intencional. Os indices devem ser adicionados apenas quando os casos de uso e consultas reais exigirem isso.

### Auth + Firestore no app

O app passou a usar Firebase real no fluxo de autenticacao:

- `Firebase Authentication` para login com e-mail e senha
- `Cloud Firestore` para manter o perfil leve do usuario em `users/{uid}`

Ao entrar:

1. o app autentica no Firebase Auth
2. busca ou cria o documento `users/{uid}`
3. monta a `AuthSession` com `email`, `displayName` e `studioId`

Se `studioId` estiver nulo, o app segue para o placeholder de onboarding.

### Emulators

Foi deixada uma configuracao base de emuladores para desenvolvimento local:

- Auth: `9099`
- Firestore: `8080`
- Emulator UI: `4000`

Isso permite testar autenticacao e regras sem depender imediatamente do ambiente online.

## Estrategia de seguranca adotada

As regras do Firestore seguem uma direcao simples para o MVP:

- todos os dados de negocio ficam abaixo de `studios/{studioId}`
- o perfil do usuario autenticado fica em `users/{uid}`
- leitura e escrita exigem usuario autenticado
- o usuario precisa pertencer ao estudio acessado para ler e escrever dados internos do estudio
- o `studioId` do documento deve ser compativel com o `studioId` do caminho

### Estrutura esperada

As regras foram desenhadas assumindo a seguinte base:

- `users/{userId}`
- `studios/{studioId}`
- `studios/{studioId}/members/{userId}`
- `studios/{studioId}/clients/{clientId}`
- `studios/{studioId}/appointments/{appointmentId}`
- `studios/{studioId}/packages/{packageId}`
- `studios/{studioId}/payments/{paymentId}`

Essa direcao reduz risco de vazamento entre tenants e deixa a modelagem legivel para o MVP.

## Premissas importantes das regras

As regras atuais assumem:

1. O usuario autenticado pode ler e atualizar apenas `users/{uid}`.
2. O acesso aos dados internos do estudio depende de um documento de membership em `studios/{studioId}/members/{uid}`.
3. O bootstrap de estúdio pode ser feito pelo proprio usuario autenticado, criando o documento do estudio e seu membership inicial.

Sem membership, o usuario continua autenticado, mas fica fora da area de dados do estudio e cai no onboarding.

## Decisoes pragmaticas

- Nao inicializamos `Functions` ainda.
- Nao adicionamos `Storage Rules` ainda.
- Nao adicionamos `Hosting`.
- Nao adicionamos indices especulativos.
- Nao dependemos de `custom claims` para o bootstrap inicial do MVP.

Essas pecas devem entrar apenas quando houver necessidade real no fluxo do MVP.

## Comandos uteis

### Validar o projeto vinculado

```powershell
firebase use
```

### Fazer deploy das regras do Firestore

```powershell
firebase deploy --only firestore:rules
```

### Fazer deploy dos indices do Firestore

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

- Os arquivos `composeApp/google-services.json` e `iosApp/iosApp/GoogleService-Info.plist` foram adicionados localmente e estao no `.gitignore`.
- O provider `Email/Password` ainda precisa estar habilitado no console do Firebase Authentication.
- O projeto iOS foi preparado com `GoogleService-Info.plist` e `FirebaseApp.configure()`, mas a vinculacao do SDK nativo da Apple ainda depende de abrir o projeto no Xcode e adicionar os pacotes Firebase.
- O `iOSApp.swift` agora protege o import de `FirebaseCore` com `#if canImport(FirebaseCore)`, evitando quebra local enquanto o pacote nativo ainda nao foi adicionado no Xcode.
- Se o onboarding inicial do estudio ainda nao estiver definido, o usuario autenticado continuara caindo no fluxo de onboarding com `studioId = null`.

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

## Firestore foundation update

The project now has a canonical Firestore model for the MVP:

- `users/{uid}`
- `studios/{studioId}`
- `studios/{studioId}/members/{uid}`
- `studios/{studioId}/clients/{clientId}`
- `studios/{studioId}/appointments/{appointmentId}`
- `studios/{studioId}/packages/{packageId}`
- `studios/{studioId}/packageLedger/{entryId}`
- `studios/{studioId}/payments/{paymentId}`

The onboarding flow is now responsible for materializing the tenant:

1. create `studios/{studioId}`
2. create `members/{uid}`
3. update `users/{uid}` with `studioId` and `role`

This keeps the backend ready for the MVP without creating demo operational data.
