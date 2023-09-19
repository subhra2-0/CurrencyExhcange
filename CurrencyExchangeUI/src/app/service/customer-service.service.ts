import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';

import axios from 'axios';
import { BehaviorSubject, Observable } from 'rxjs';
import { Customers } from '../admin/customers';
import { FormGroup } from '@angular/forms';
import { AddCustomerComponent } from '../admin/add-customer/add-customer.component';
import { Constants } from 'src/model/constants';
import { Router } from '@angular/router';


@Injectable({
  providedIn: 'root'
})
export class CustomerServiceService {
 
  showErrorMessageAlert:boolean= false;
  errorMessage: string | null = null;


  private allRecordsSource = new BehaviorSubject<Customers[]>([]);
  allRecords$ = this.allRecordsSource.asObservable();

  setAllRecords(records: Customers[]) {
    this.allRecordsSource.next(records);
  }

  throwError(error: any) {
    if (error instanceof HttpErrorResponse && error.status === 403) {
      // Handle 403 Forbidden error
      alert("Session Expired, Redirecting to Login Page");
        localStorage.clear();
        this.router.navigate(["login"]);
    } 
    else if(error instanceof HttpErrorResponse && error.status === 401)
    {
      this.showErrorAlertFunc('Invalid Credentials !');
    }
  }

  showErrorAlertFunc(message:any)
  {
    this.showErrorMessageAlert = true;
    this.errorMessage = message;
    setTimeout(() => {
      this.hideAlertFunc(); 
    }, 5000);
  }
  hideAlertFunc()
  {
    this.showErrorMessageAlert=false;
    this.errorMessage = null;
  }


  customerId:any;
  editEmail:any;
  loggedIn:boolean=false;
  // url=" http://localhost:3000/customers";

  // postCustomers(data:any){
  //   return axios.post(this.url,data)
  // }
  // getCustomers(){
  //   return axios.get(this.url)
  // }

baseURL=Constants.baseURL;
baseUrl1=Constants.baseUrl1;
baseUrl2=Constants.baseUrl2;
baseUrl3=Constants.baseUrl3;


token:any;


url="http://172.16.238.163:8080/authenticate/login";


constructor(private httpClient: HttpClient,
  private router:Router) { }

isloggedIn(){
  var data=localStorage.getItem("loginstatus");
  if(data==="success")
  {
    return true;
  }
  else{
    return false;
  }
}
  
getCustomersList(): Observable<Customers[]> {
  this.token=localStorage.getItem("token");
  console.log(this.token);
  console.log(localStorage.getItem("token"));
  
  const headers = { 'Authorization': 'Bearer ' + this.token };
  
  // Use template literals to construct the URL
  const url = `${this.baseURL}`;
  
  return this.httpClient.get<Customers[]>(url, { headers });
}

createCustomer(customer:Customers): Observable<Object>{
 
  const headers = { 'Authorization': 'Bearer ' + this.token };
  console.log(customer);
  const url = `${this.baseUrl1}`;

 return this.httpClient.post(url, customer,{headers});
}

updateCustomer(customerId: number, customer: Customers): Observable<Object>{
  
  console.log(customer);
  console.log(customerId);
  const headers = { 'Authorization': 'Bearer ' + this.token };
  const url = `${this.baseUrl2}/${customerId}`
  
return this.httpClient.put(url, customer,{headers});
}


deleteCustomer(customerId:number): Observable<Object>{

  console.log(customerId);
  const headers = { 'Authorization': 'Bearer ' + this.token };

  const url =`${this.baseUrl3}/${customerId}`;
  return this.httpClient.delete(url,{headers});
  // return this.httpClient.delete(url,{ responseType: 'text' });
}

login(data:any):Observable<any>{
  return this.httpClient.post(this.url,data);
}


}





