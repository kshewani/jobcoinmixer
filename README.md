This is well known that most cryptocurrencies are pseudonymous. This means, these are not fully anonymous and transactions can be traced to an individual following his/her address.

This coin mixer application helps achieve this anonymity by transferring the coins to a bunch of addresses instead of just one. This is how it works:
1. A user submits a mixing request. A mixing request contains the following fields:
    - The amount to be transferred.
    - One or more address(es) to which the coins are to be transferred.
    - The way amount is to be split across addresses. The application currently allows equal and random splits.
2. Upon submission of the request, the user needs to transfer the exact amount as specified in the mixing request to the mixer address (KAM_MIXER).
3. Once the mixer receives the amount, the mixer transfers the coins to a house account.
4. Mixer then transfers coins to each destination address after deducting a mixing fee (0.01%).

![Flow diagram](jobcoinmixer_flow.jpg)

Note: Currently the application can only accept mixing requests in JSON format from the command line. For the sake of demonstration, one sample JSON request is hardcoded in the application.

#### Sample mixing request json:
>{
"sourceAddress": "Kam",
"accounts": [
{
"address": "Alice",
"amount": 0.0
},
{
"address": "Bob",
"amount": 0.0
},
{
"address": "Hazy",
"amount": 0.0
}
],
"amount": "15",
"splitType": "Equal"
}

## How to run:
Run below command from command prompt
>java -jar .\out\artifacts\jobcoinmixer_jar\jobcoinmixer.jar

## Technical details:
Following are the important components of the application:
1. #### MixingRequestService:
    - Accepts mixing request service.
    - Calculates the amount to be paid to each destination address based on the split type mentioned in the mixing request (Equal/Random).
    - Submits the mixing request to MixingService for further processing.
2. #### TransactionService:
    - Polls for transactions based on the configured interval.
    - Finds transactions that are paid to mixer address.
    - Sends the matching transactions to MixingService.
3. #### MixingService:
    - Receives transactions paid to mixer address from TransactionService.
    - Maps transactions to mixing requests by comparing the source address.
    - Performs validation on transactions.
    - If not found valid, refunds the coins back to the source address.
    - Transfer coins to house address.
    - Transfer coins from house address to each destination account after deducing the mixing fee.
4. #### EventQueue:
    - An in-memory broker to enable event-based execution of mixing actions.
    - Each matching transaction is added as an event to TransactionQueue and subsequently processed by MixingSerivce.
    - Every transfer to a destination address is added as an event to MixingQueue.
    - Enables concurrent execution of mixing events.
5. #### CoinSplitAlgoFactory:
   - Creates a corresponding algorithm to decide how much coins are to be transferred to each destination address.

## Path to production:
1. A friendly UI would be required to accept mixing request details from users and submit the same to JobCoinMixer in JSON format via REST api.
2. MixingRequestService, TransactionService, and MixingService should be turned into separate microservices which can run on their own.
3. Event or message streaming platforms like [RabbitMq](https://www.rabbitmq.com/) / [Kafka](https://kafka.apache.org/) can be used in place of EventQueue.


## Excerpts from jobcoinmixer logs:
```
JobcoinmixerApplication - Starting jobcoinmixer application.
services.TransactionService - Polling for transactions.
jobcoinmixer.services.RestClient - Fetching transactions from REST server.
services.MixingRequestService - Received mixing request:
services.MixingRequestService - {"sourceAddress":"Kam","accounts":[{"address":"Alice","amount":0.0,"isAmountTransferred":false},{"address":"Bob","amount":0.0,"isAmountTransferred":false},{"address":"Hazy","amount":0.0,"isAmountTransferred":false}],"amount":"15","splitType":"Equal","complete":false}
services.MixingRequestService - Mixing request post initialization. Id: 1
services.MixingRequestService - {"sourceAddress":"Kam","accounts":[{"address":"Alice","amount":4.95,"isAmountTransferred":false},{"address":"Bob","amount":4.95,"isAmountTransferred":false},{"address":"Hazy","amount":5.1000000000000005,"isAmountTransferred":false}],"amount":"15","splitType":"Equal","complete":false}
services.TransactionService - Polling for transactions.
jobcoinmixer.services.RestClient - Fetching transactions from REST server.
jobcoinmixer.services.RestClient - Transaction details: {"isRefunded":false,"fromAddress":"KAM_MIXER","toAddress":"KAM_MIXER_HOUSE","amount":"15","timestamp":"2021-09-07T23:29:50.928UTC"}
services.MixingService - Transferred 15 coins to house account.
services.MixingService - Adding transaction to mixing queue.
services.MixingService - Performing mixing transaction for mixing id: 1
jobcoinmixer.services.RestClient - Transaction details: {"isRefunded":false,"fromAddress":"KAM_MIXER_HOUSE","toAddress":"KAM_MIXER","amount":"0.0495","timestamp":"2021-09-07T23:29:51.836UTC"}
services.MixingService - Transferred 0.049500 coins to mixer account to pay mixing fee.
services.MixingService - Mixing request id: 1, Transferring 4.9005 to Alice after deducting 0.049500 mixing fee.
jobcoinmixer.services.RestClient - Transaction details: {"isRefunded":false,"fromAddress":"KAM_MIXER_HOUSE","toAddress":"Alice","amount":"4.9005","timestamp":"2021-09-07T23:29:52.749UTC"}
services.MixingService - Mixing request id: 1, Transferred 4.9005 to Alice after deducting 0.049500 mixing fee.
services.MixingService - Performing mixing transaction for mixing id: 1
jobcoinmixer.services.RestClient - Transaction details: {"isRefunded":false,"fromAddress":"KAM_MIXER_HOUSE","toAddress":"KAM_MIXER","amount":"0.0495","timestamp":"2021-09-07T23:29:53.672UTC"}
services.MixingService - Transferred 0.049500 coins to mixer account to pay mixing fee.
services.MixingService - Mixing request id: 1, Transferring 4.9005 to Bob after deducting 0.049500 mixing fee.
jobcoinmixer.services.RestClient - Transaction details: {"isRefunded":false,"fromAddress":"KAM_MIXER_HOUSE","toAddress":"Bob","amount":"4.9005","timestamp":"2021-09-07T23:29:54.590UTC"}
services.MixingService - Mixing request id: 1, Transferred 4.9005 to Bob after deducting 0.049500 mixing fee.
services.MixingService - Performing mixing transaction for mixing id: 1
jobcoinmixer.services.RestClient - Transaction details: {"isRefunded":false,"fromAddress":"KAM_MIXER_HOUSE","toAddress":"KAM_MIXER","amount":"0.051","timestamp":"2021-09-07T23:29:55.512UTC"}
services.MixingService - Transferred 0.051000 coins to mixer account to pay mixing fee.
services.MixingService - Mixing request id: 1, Transferring 5.049 to Hazy after deducting 0.051000 mixing fee.
jobcoinmixer.services.RestClient - Transaction details: {"isRefunded":false,"fromAddress":"KAM_MIXER_HOUSE","toAddress":"Hazy","amount":"5.049","timestamp":"2021-09-07T23:29:56.435UTC"}
services.MixingService - Mixing request id: 1, Transferred 5.049 to Hazy after deducting 0.051000 mixing fee.
services.MixingService - Mixing request with ID 1 successfully completed...
```
