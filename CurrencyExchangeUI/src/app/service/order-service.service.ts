import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Accounts } from '../admin/accounts';
import { Observable } from 'rxjs';
import { Constants } from 'src/model/constants';
import { ConversionResponse, ConversionRequest } from '../order/conversion';

@Injectable({
  providedIn: 'root'
})
export class OrderServiceService {

  
  conversion:ConversionRequest=new ConversionRequest();

  customerId:any;
  token:any;
  constructor(private httpClient: HttpClient) { }
  getAccountsList(customerId: number): Observable<Accounts[]> {
    this.token=localStorage.getItem("token");
    console.log(this.token);
    const headers = { 'Authorization': 'Bearer ' + this.token };
    console.log(customerId);
    // Use template literals to construct the URL
    const url = `${Constants.baseUrl4}/${customerId}`;
    console.log(url);
    return this.httpClient.get<Accounts[]>(url, { headers });
  }

  convertCurrency(conversion: any):Observable<Object>
  {
    this.token=localStorage.getItem("token");
    const headers = { 'Authorization': 'Bearer ' + this.token };
    console.log(conversion);
    const url = `${Constants.baseUrl5}`;
    console.log(url);
    return this.httpClient.post<Object>(url, conversion, { headers });
  }

  conversionHistory(customerId: number):Observable<Object>
  {
    this.token=localStorage.getItem("token");
    const headers = { 'Authorization': 'Bearer ' + this.token };
    console.log(customerId);
    // Use template literals to construct the URL
    const url = `${Constants.baseUrl6}/${customerId}`;
    console.log(url);
    return this.httpClient.get<Object[]>(url, { headers });
  }

}
