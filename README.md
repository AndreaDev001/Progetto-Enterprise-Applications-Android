# Progetto-Enterprise-Applications-Android
Front-End Android per il corso di Enterprise-Applications, utilizza **Jetpack Compose**
Sviluppato da Andrea Marchio, **223401**
Per ottenere i dati, effettua delle richieste HTTP utilizzando **Retrofit** ad un resource server, il cui codice è disponibile nella seguente repository: [Progetto-Enterprise-Applications-REST-API](https://github.com/AndreaDev001/Progetto-Enteprise-Applications-REST-API) </br>
Per eseguire l'autenticazione degli utenti, utilizza un authorization server, il cui codice è disponibile nella seguente repository: [Progetto-Enterprise-Applications-Authentication](https://github.com/AndreaDev001/Progetto-Enterprise-Applications-Authentication) </br>
Se si utilizza l'emulatore è necessario sostituire dentro il file **RetrofitConfig**, la variabile **resourceServerIpAddress** con **10.0.2.2:SERVER_PORT**, se l'applicazione verrà eseguita su telefono sarà necessario
sostuire il valore della variabile con il proprio indirizzo ip **ip_address:SERVER_PORT**. </br>
Se questa variabile non è impostata non sarà possibile effettuare richieste al resource server e di conseguenza utilizzare l'applicazione


