import { Component, OnInit } from '@angular/core';
import { OrderModule } from '../order.module';
import { OrderServiceService } from 'src/app/service/order-service.service';
import { Accounts, CurrencyTypeEnum } from '../../admin/accounts';
import { ActivatedRoute, Data, Router } from '@angular/router';
import { ConversionRequest } from '../conversion';
import { MatDialog } from '@angular/material/dialog';
import { ConversionSuccessDialogComponent } from '../conversion-success-dialog/conversion-success-dialog.component';
import { FormBuilder, FormGroup, NgForm, Validators } from '@angular/forms';
import { LoaderService } from 'src/app/service/loader.service';
import { take, timer } from 'rxjs';
import { Constants } from 'src/model/constants';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-place-order',
  templateUrl: './place-order.component.html',
  styleUrls: ['./place-order.component.css']
})
export class PlaceOrderComponent implements OnInit {
  myForm:FormGroup;
  accounts: Accounts[] = [];
  conversion: ConversionRequest = new ConversionRequest();
  
  fetchCurrencyType:boolean=false;

  selectedDebitAccount: any;
  selectedCreditAccount: any;
  validateAccountBalance:boolean =false;
  enableFetchRates: boolean = false;
  amount: any;
  currencyRatesMessage:any;
  balanceMessage:any;
  fetchedDrcurrencyType:any;
  fetchedCrcurrencyType:any;
   compareCreditAccount:any;
   compareDebitAccount:any;
   conversionRates:any;
  crCurrency:any =null;
  accountBalances: { [accountNumber: string]: string } = {};
  validateAccountBalanceList: { [accountNumber: number]: number } = {};
  AccountCurrencyType: { [accountNumber: number]: CurrencyTypeEnum } = {};
  customerId: number;
  fetchBalance:number;
  constructor(private orderservice: OrderServiceService,
    private dialog: MatDialog,private fb: FormBuilder,
    private router: Router, private route: ActivatedRoute,
    public loaderservice: LoaderService,
    private httpClient:HttpClient) {
  }

  ngOnInit(): void {

    this.myForm = this.fb.group({
      selectedDebitAccount: ['', Validators.required],
      selectedCreditAccount: ['', Validators.required],
      amount: ['', [Validators.required, Validators.min(0)]]
    });
   
    if (localStorage.getItem('role') !== 'CUSTOMER') {
      localStorage.clear();
      setTimeout(() => {
        alert('You are not Authorized to Visit this Page, Kindly login again with valid Credentials');
      }, 1);
      this.router.navigate(['login'])
    }
    else if (localStorage.getItem('role') === 'CUSTOMER') {
      this.customerId = +localStorage.getItem('id');
      this.orderservice.getAccountsList(this.customerId).subscribe(data => {
        this.accounts = data;  
      },
    
        (error)=>{
          setTimeout(() => {
            alert("Session expired, Kindly Login again to resume your Journey on the Portal");
            console.log(error); 
            localStorage.clear(); 
          },1);
      }
      ) 
    }
  }
  validateAccountBalanceFunc()
  {
    if(this.selectedCreditAccount!=undefined && this.selectedDebitAccount!=undefined && this.selectedCreditAccount==this.selectedDebitAccount)
    {
      this.validateAccountBalance = true;
      this.enableFetchRates = false;
      this.balanceMessage ="Debit/Credit Accounts cannot be same."
      this.myForm.get('amount').patchValue('');
      this.myForm.get('selectedDebitAccount').setErrors
      ({
        invalid:true
      })
    }
    else if(this.selectedDebitAccount)
    {
      if(this.selectedCreditAccount==undefined && this.myForm.get('amount').value!=undefined)
      {
        this.validateAccountBalance=true;
        this.enableFetchRates = false;
        this.balanceMessage = "Credit Account not selected";
        this.myForm.get('amount').patchValue('');
        this.myForm.get('amount').setErrors({
          invalid:true
        });
      }
      else if(this.myForm.get('amount').value>this.validateAccountBalances(this.selectedDebitAccount))
      {
        this.fetchBalance = this.validateAccountBalances(this.selectedDebitAccount)
        this.validateAccountBalance=true;
        this.enableFetchRates = false;
        this.balanceMessage = "Insufficient Funds in Debit Account";
        this.myForm.get('amount').setErrors({
          invalid:true
        });
      }
      else if(this.myForm.get('amount').value !==null && this.myForm.get('amount').value<=0)
      {
       this.validateAccountBalance=true;
       this.enableFetchRates = false;
         this.balanceMessage = "Mininum Amount required is 1";
         this.myForm.get('amount').setErrors({invalid:true})
      }
      else
      {
        this.validateAccountBalance=false;
        this.balanceMessage=null;
         if(this.myForm.get('amount').value!==undefined)
           {this.enableFetchRates = true;}
        this.myForm.get('selectedDebitAccount').setErrors(null);
      }
   }
   else
   {
    this.validateAccountBalance=true;
    this.enableFetchRates = false;
    this.myForm.get('amount').patchValue('');
      this.balanceMessage = "Debit Account Not Selected";
   }
  }
  fetchRates()
  {
    console.log("inside fetchRates");
    if(this.selectedCreditAccount!=undefined && this.selectedDebitAccount!=undefined && this.selectedCreditAccount!==this.selectedDebitAccount)
    {
      this.balanceMessage = null;
      this.validateAccountBalance=false;
      this.enableFetchRates = true;
      const conversionRate=`${Constants.conversionAPI}/${this.AccountCurrencyType[this.selectedDebitAccount]}`;
      const crType = this.AccountCurrencyType[this.selectedCreditAccount];
      if( this.compareDebitAccount !== this.selectedDebitAccount )
      {
        this.compareDebitAccount = this.selectedDebitAccount;
        this.conversionRates = this.httpClient.get<any>(conversionRate);
          this.conversionRates.subscribe((data:any) => {
            console.log(data);
            this.crCurrency = data.rates;
            console.log(this.crCurrency);
           }) 
      }
     
   setTimeout(()=>{
    this.currencyRatesMessage = `1 ${this.AccountCurrencyType[this.selectedDebitAccount]} = ${this.crCurrency[crType]} ${this.AccountCurrencyType[this.selectedCreditAccount]}<sup>*</sup>`;
   },100)
    
    } 
   
  }


  cancel(): void {

  }

  submit(): void {
    
      this.conversion.drAccount = this.selectedDebitAccount;
      this.conversion.crAccount = this.selectedCreditAccount;
      this.conversion.conversionAmount = this.myForm.get('amount').value;
      console.log(this.conversion);

      this.orderservice.convertCurrency(this.conversion).subscribe(
        (conversiondata) => {
          console.log(conversiondata);
          this.loaderservice.showLoader();
          timer(1000)
            .pipe(take(1))
            .subscribe(() => {
              this.loaderservice.hideLoader();  
        const dialogRef= this.dialog.open(ConversionSuccessDialogComponent, {
            width:'400px',
            data:{
              txn:conversiondata
            }
          })
          dialogRef.afterOpened().subscribe(() => {
            setTimeout(() => {
              dialogRef.close();
            }, 4000);
            this.router.navigate(['orderwatchlist']);
          });
          dialogRef.afterClosed().subscribe(() =>{
             this.orderservice.getAccountsList(this.customerId).subscribe(data => {
               this.accounts = data;
            })
          })
        }) 
        },
        (error) => {
          console.error('HTTPS error:', error);
        }
      );
    
    
  }
  getAccountBalance(accountNumber: string): string {
    return this.accountBalances[accountNumber] || '0';
  }
  validateAccountBalances(accountNumber: number): number {
    return this.validateAccountBalanceList[accountNumber] || 0;
  }

 getSelectedDebitAccountBalance(): void {
  if (this.selectedDebitAccount) {
    this.validateAccountBalance=false;
    this.fetchedDrcurrencyType = this.selectedDebitAccount;
    const selectedAccountNumber = Number(this.selectedDebitAccount);
   
    const selectedAccount = this.accounts.find(
      (account) => account.accountNumber === selectedAccountNumber
    );
    if (selectedAccount) {
      this.accountBalances[selectedAccountNumber] = selectedAccount.balance + " " + selectedAccount.currencyType;
      this.validateAccountBalanceList[selectedAccountNumber]=selectedAccount.balance;
      this.AccountCurrencyType[selectedAccountNumber]=selectedAccount.currencyType;
      

    }
  }
}

getSelectedCreditAccountBalance() {
  if (this.selectedCreditAccount) {
    this.fetchedCrcurrencyType = this.selectedDebitAccount;
    const selectedAccountNumber = Number(this.selectedCreditAccount);
    const selectedAccount = this.accounts.find((account) => account.accountNumber === selectedAccountNumber);
    if (selectedAccount) {
      this.accountBalances[selectedAccountNumber] = selectedAccount.balance + " " + selectedAccount.currencyType;
      this.AccountCurrencyType[selectedAccountNumber]=selectedAccount.currencyType;
    }
  }
}

}


