export class ConversionResponse {
    txnId:number;
    drAccount:number;
    crAccount:number;
    conversionAmount:number;
    remarks:string;
}

export class ConversionRequest
{
    drAccount:number;
    crAccount:number;
    conversionAmount:number;
}