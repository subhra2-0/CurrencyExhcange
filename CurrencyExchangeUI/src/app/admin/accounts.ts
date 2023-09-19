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
export class accountBalanceDetailsRequest
{
    drCr_flg:drCr_flgEnum
    amount:number
}
export enum drCr_flgEnum
{
    CREDIT='CREDIT',
    DEBIT='DEBIT'
}
