export enum CurrencyTypeEnum
{
    INR ='INR',
    USD='USD',
    PLN='PLN',
    SGD='SGD'
}
export class Accounts
{
    accountNumber: number
    balance: number
    currencyType: CurrencyTypeEnum
    accountCreationDate: string
    accHolderName: string
}
