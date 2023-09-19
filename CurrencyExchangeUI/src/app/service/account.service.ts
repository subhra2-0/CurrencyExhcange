import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Accounts, accountBalanceDetailsRequest } from '../admin/accounts';
import { Constants } from 'src/model/constants';
import { RegisterAccount } from '../admin/register-account';
@Injectable({
  providedIn: 'root'

})

export class AccountService {
  
  createAccount(customerId: number,accountdetails:RegisterAccount) {
    this.token=localStorage.getItem("token");
    console.log(this.token);
    const headers = { 'Authorization': 'Bearer ' + this.token };
    // Use template literals to construct the URL
    const url = `${Constants.baseUrl7}/${customerId}`;
    console.log(url);
    return this.httpClient.post<Accounts[]>(url,accountdetails, { headers });
  }

 token:any;

  constructor(private httpClient: HttpClient) { }

  getAccountsList(): Observable<Accounts[]> {
    this.token=localStorage.getItem("token");
    console.log(this.token);
    const headers = { 'Authorization': 'Bearer ' + this.token };
    // Use template literals to construct the URL
    const url = `${Constants.baseUrl4}`;
    console.log(url);
    return this.httpClient.get<Accounts[]>(url, { headers });
  }
  updateAccount(customerId: number,accountNumber:number,accountBalanceDetailsRequest:accountBalanceDetailsRequest) {
   console.log(accountBalanceDetailsRequest);
    this.token=localStorage.getItem("token");
    console.log(this.token);
    const headers = { 'Authorization': 'Bearer ' + this.token };
    const url = `${Constants.baseUrl8}/${customerId}/${accountNumber}`;
    console.log(url);
    return this.httpClient.put<Accounts>(url,accountBalanceDetailsRequest, { headers });
  }    

  deleteAccount(customerId:number,accountNumber:number): Observable<Object>{
   this.token=localStorage.getItem("token");
    console.log(customerId,accountNumber);
    const headers = { 'Authorization': 'Bearer ' + this.token };
    const url =`${Constants.baseUrl9}/${customerId}/${accountNumber}`;
    return this.httpClient.delete(url,{headers});
  }
}

 