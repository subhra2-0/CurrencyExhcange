import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { CreateAccountDailogComponent } from '../create-account-dailog/create-account-dailog.component';
import { Accounts } from 'src/app/admin/accounts';
import { OrderServiceService } from 'src/app/service/order-service.service';
import { ActivatedRoute, Router } from '@angular/router';
import { LoaderService } from 'src/app/service/loader.service';
import { timer, take } from 'rxjs';
import { Customers } from '../customers';
import { HttpErrorResponse } from '@angular/common/http';
import { CustomerServiceService } from 'src/app/service/customer-service.service';
import { UpdateAccountDailogComponent } from '../update-account-dailog/update-account-dailog.component';
import { DeleteConfirmationDialogComponent } from '../delete-confirmation-dialog/delete-confirmation-dialog.component';
import { AccountService } from 'src/app/service/account.service';

@Component({
  selector: 'app-account-list',
  templateUrl: './account-list.component.html',
  styleUrls: ['./account-list.component.css']
})
export class AccountListComponent implements OnInit {
  displayedColumns: string[] = ['accountNumber', 'balance', 'currencyType', 'accountCreationDate', 'accHolderName', 'actions'];
  accounts: Accounts[] = [];

  customer: Customers;
  id: number;
  showSuccessAlert: boolean = false;
  showErrorAlert:boolean =false;
  alertMessage:any;

  constructor(private orderservice: OrderServiceService,
    private customerservice: CustomerServiceService,
    private route: ActivatedRoute,
    private dialog: MatDialog,
    private router: Router,
    private accountservice: AccountService,
    public loaderservice: LoaderService
  ) {  }

  dialogRef: any;




  openCreateAccountDialog(customerId: number): void {
    this.loaderservice.showLoader();
    timer(1500)
      .pipe(take(1))
      .subscribe(() => {
        this.loaderservice.hideLoader();
        this.dialogRef = this.dialog.open(CreateAccountDailogComponent, {
          width: '400px',
          data: {
            customer: {
              customerId: customerId,
            }
          }
        });

        this.dialogRef.afterClosed().subscribe((result: any) => {
          if (result === true) {
            this.getAccountsList(customerId);
            this.alertMessage = 'Account Created Successfully';
             this.showSuccessAlertFunc();
          }
          else if( result instanceof HttpErrorResponse && result.status==406)
          {
            this.alertMessage = 'Account Already Exists';
            this.showErrorAlertFunc();
          }
          else if( result instanceof HttpErrorResponse && result.status==403)
          {
            this.alertMessage = 'Session Expired, Redirecting to Login';
            localStorage.clear(); 
            this.showErrorAlertFunc();
            setTimeout(()=>
            {
              this.router.navigate(['login']);
            },5000); 
          }
          else if(result!==undefined)
          {
            this.alertMessage = 'Unknown Technical Exception';
            this.showErrorAlertFunc();
          }
        }
        )
      })
  }

  showSuccessAlertFunc()
  {
    this.showSuccessAlert=true;
    setTimeout(() =>{
      this.hideSuccessAlertFunc();
    },5000)
  }

  hideSuccessAlertFunc()
  {
    this.showSuccessAlert=false;
  }

  showErrorAlertFunc()
  {
    this.showErrorAlert=true;
   
    setTimeout(() =>{
      this.hideErrorAlertFunc();
    },5000)
  }

  hideErrorAlertFunc()
  {
    this.showErrorAlert=false;
  }
  parseDateString(dateString: string): Date {
    
    const parts = dateString.split(' ');
    const datePart = parts[0];
    const timePart = parts[1];

    
    const dateParts = datePart.split('-');
    const day = parseInt(dateParts[0], 10);
    const month = parseInt(dateParts[1], 10) - 1; 
    const year = parseInt(dateParts[2], 10);

    
    const timeParts = timePart.split(':');
    const hours = parseInt(timeParts[0], 10);
    const minutes = parseInt(timeParts[1], 10);
    const seconds = parseInt(timeParts[2], 10);


    return new Date(year, month, day, hours, minutes, seconds);
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

  ngOnInit(): void {
    this.id = +this.route.snapshot.params['customerId'];
    this.getAccountsList(this.id);
  }

  getAccountsList(customerId: any) {
    this.orderservice.getAccountsList(customerId).subscribe((data: any) => {
      console.log(data);
      this.accounts = data;
    }, (error) => {
      this.customerservice.throwError(error);
    });
  }

  openUpdateAccountDialog(customerId:number,accountNumber: number): void {
    this.loaderservice.showLoader();
    timer(1500)
      .pipe(take(1))
      .subscribe(() => {
        this.loaderservice.hideLoader();
        this.dialogRef = this.dialog.open(UpdateAccountDailogComponent, {
          width: '400px',
          data: {
            accounts: {
              accountNumber: accountNumber,
            },
            customer:{
              customerId:customerId,
            }
          }
        });
           this.dialogRef.afterClosed().subscribe((result: any) => {
            if (result === true) {
              this.getAccountsList(customerId);
              this.alertMessage = 'Account Balance updated Successfully';
               this.showSuccessAlertFunc();
            }
            else if( result instanceof HttpErrorResponse && result.status==403)
            {
              this.alertMessage = 'Session Expired, Redirecting to Login';
              localStorage.clear(); 
              this.showErrorAlertFunc();
              setTimeout(()=>
              {
                this.router.navigate(['login']);
              },5000); 
            }
            else if(result!==undefined)
            {
              this.alertMessage = 'Unknown Technical Exception';
              this.showErrorAlertFunc();
            }
        }
        )
      })
  }
 openDeleteConfirmationDialog(customerId: number, accountNumber:number): void {
    const dialogRef = this.dialog.open(DeleteConfirmationDialogComponent, {
      width: '400px',
      data:{
        myString: "Account ?"
      }
    });
    dialogRef.afterClosed().subscribe(result => {
      if (result === true) {
        this.accountservice.deleteAccount(customerId,accountNumber).subscribe(
        (data: any) => {
            this.getAccountsList(customerId);
            this.alertMessage = 'Account Deleted Successfully';
               this.showSuccessAlertFunc();
          },
          (error) => {
            if (error instanceof HttpErrorResponse && error.status==403) 
            {
              this.alertMessage = 'Session Expired, Redirecting to Login';
            localStorage.clear(); 
            this.showErrorAlertFunc();
            setTimeout(()=>
            {
              this.router.navigate(['login']);
            },5000); 
              console.log(error.error);
            }
            else 
            {
              this.alertMessage='Unknown Technical Exception';
              this.showErrorAlertFunc();
            }
          }
        );
      }
    });
  }
}

