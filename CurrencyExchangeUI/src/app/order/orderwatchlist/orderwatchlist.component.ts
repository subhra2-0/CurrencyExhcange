import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { OrderServiceService } from 'src/app/service/order-service.service';


// import {MatIconModule} from '@angular/material/icon';
// import {MatButtonModule} from '@angular/material/button';
// import {MatToolbarModule} from '@angular/material/toolbar';

interface Currency {
  txnId: number,
  drAccountType: number,
  crAccountType: number,
  conversionAmount: number,
  conversionUnitRate:number
  transactionDate: Date
}

@Component({
  selector: 'app-orderwatchlist',
  templateUrl: './orderwatchlist.component.html',
  styleUrls: ['./orderwatchlist.component.css'] ,
  // standalone: true,
  // imports: [MatToolbarModule, MatButtonModule, MatIconModule],

})
export class OrderwatchlistComponent implements OnInit {
  // ngOnInit(): void {
  //   throw new Error('Method not implemented.');
  // }

  pageSize: number = 5; // Number of items per page
  currentPage: number = 1; // Current page number
    
  currencies: Currency[] = [];
  customerId:number;

  constructor(private http: HttpClient,private orderservice:OrderServiceService) {}

  
 // currencies:Currency[] = currencyData;
  headerText = '';
  // currencies = [{ pair: 'USD/INR', rate: 74.5, amount: 1000, date: new Date() },
  // { pair: 'PLN/SGD', rate: 0.25, amount: 500, date: new Date() },
  // { pair: 'SGD/INR', rate: 54.2, amount: 800, date: new Date() },
  // { pair: 'USD/PLN', rate: 3.8, amount: 700, date: new Date() }];

  showRecentlyTraded() {
    this.headerText = 'RECENTLY TRADED';
 //   this.currencies = ["USD","INR","PLN","SGD"];
    

}
ngOnInit(): void {
  this.fetchCurrenciesData();
}

fetchCurrenciesData(): void {

  this.customerId = +localStorage.getItem('id');
  this.orderservice.conversionHistory(this.customerId).subscribe((data:any)=>{
    this.currencies=data.currencyConversionResponse;
    console.log(data);
  });
}
onPageChange(pageNumber: number): void {
  this.currentPage = pageNumber;
}
getPageNumbers(): number[] {
  const pageCount = Math.ceil(this.currencies.length / this.pageSize);
  return Array(pageCount)
    .fill(0)
    .map((_, index) => index + 1);
}

getPageStartIndex(): number {

  return (this.currentPage - 1) * this.pageSize;

}



getPageEndIndex(): number {

  return this.getPageStartIndex() + this.pageSize;

}

}
