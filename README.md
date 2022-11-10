# Progetto-PO

## TITOLO
# AEREODATA REST API

## AUTORI

### Aurelio Pacetti 50%
### William Properzi 50%




## INTRODUZIONE
AEREODATA REST API gestisce i dati restituiti dalla chiamata all'API di aereodata.p.rapidapi ricercando i voli e gli aereoporti .  

## DESCRIZIONE ED UTILIZZO

L'esecuzione prevede la proposta di diverse funzionalità, tramite utilizzo di rotte (Paths) , da parte dell'applicazione per l'utente, il quale si occupa di assegnare i valori alle keys.

Le KEYS a cui assegnare i VALUES sono:

{
- locality

}


 
### PATHS

L'applicazione viene fatta partire sul " localhost:8080 " e tramite le paths l'utente effettua le sue richieste

* localhost:8080




* (/home) 
Mostra i path che l'utente ha a disposizione:

![Immagine1](https://user-images.githubusercontent.com/63963981/201139967-49991315-402e-4c84-9369-c369094d18b0.png)

![Immagine2](https://user-images.githubusercontent.com/63963981/201141442-ba6f0f13-17c8-48a7-a050-cae4a39139e7.png)






1. ("/airports?locality=Rome")


Tipo | Path | 
---- | ---- | 
GET | localhost:8080/airports?locality=Rome|

![Immagine3](https://user-images.githubusercontent.com/63963981/201141500-6aa93c49-0afb-4764-88fc-0ab6b1c69cba.png)






Rotta di GET che mostra i dati agli aereoporti contenenti nel nome la sigla specificata nella rotta.

![Immagine4](https://user-images.githubusercontent.com/63963981/201141556-443a0df7-8712-4e8e-8f82-1deb6bc2c059.png)



* Risposta all'utente rispetto alla variabile 'locality' dell'immagine sopra

![Immagine5](https://user-images.githubusercontent.com/63963981/201141584-6c841f95-92a7-4b51-964d-c272520d913d.png)






2. ("/flights/LIRF/2022-11-05T12:00")


Tipo | Path | 
---- | ---- | 
GET | localhost:8080/flights/{airport_ICAO}/{date} |


![Immagine6](https://user-images.githubusercontent.com/63963981/201141640-2cf51716-c1ae-40a7-81e6-4b23143094c5.png)



Rotta di tipo GET che mostra i dati degli archivi relativi ai voli in riferimento al periodo temporale inserito dall'utente




![Immagine7](https://user-images.githubusercontent.com/63963981/201141697-83849e41-6346-4145-9018-34d3f916368f.png)






3. ("/flights/{airport}/{date}/stats/airlines")


Tipo | Path | 
---- | ---- | 
GET | localhost:8080/flights/{airport}/{date}/stats/airlines |


![Immagine8](https://user-images.githubusercontent.com/63963981/201141728-b6417977-385c-47fe-8e95-e23d8d800cbf.png)



Rotta di tipo GET che mostra i dati degli archivi relativi alle percentuali di carico, arrivi e partenze di voli in riferimento al periodo temporale inserito dall'utente



![Immagine9](https://user-images.githubusercontent.com/63963981/201141756-5081d12f-9573-4882-8117-0fa4bd4191f4.png)









## UML

### CASI D'USO


* UTENTE :

![usecaseUTENTE](https://user-images.githubusercontent.com/63963981/201141821-4308935a-a20b-46b2-b47c-c2bba54252b5.png)



* USERSYSTEM :



![usecaseSYSTEM](https://user-images.githubusercontent.com/63963981/201141838-6701c45d-8b89-4935-b180-2478986ee97b.png)



### DIAGRAMMA DELLE CLASSI



* CONTROLLER :


![classController](https://user-images.githubusercontent.com/63963981/201141891-d7af86df-a92e-4c11-a77f-ecc0694d4247.png)


* SERVICE : 

![serviceClass](https://user-images.githubusercontent.com/63963981/201141930-620f689d-0a1b-40da-a27b-d52214f3e442.png)



* UTILITIES :

![utilClass](https://user-images.githubusercontent.com/63963981/201141975-6a12f39c-f8ed-4a03-89a8-a9591bb61470.png)



* MODEL : 


![modelClass](https://user-images.githubusercontent.com/63963981/201142003-c7b7b035-57a3-4129-be4a-917eaa324c4a.png)








