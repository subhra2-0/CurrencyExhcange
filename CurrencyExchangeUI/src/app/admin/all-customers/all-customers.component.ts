import { Component, OnInit } from '@angular/core';
import { CustomerServiceService } from 'src/app/service/customer-service.service';
import { Customers } from '../customers';
import { ActivatedRoute, Router } from '@angular/router';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';

import { DeleteConfirmationDialogComponent } from '../delete-confirmation-dialog/delete-confirmation-dialog.component';









@Component({

  selector: 'app-all-customers',

  templateUrl: './all-customers.component.html',

  styleUrls: ['./all-customers.component.css']

})

export class AllCustomersComponent implements OnInit {

  alertMessage:any;
  showSuccessAlert: boolean = false;

  customers: Customers[] = [];
  allRecords:Customers[]=[];


  customerId: number;

  // customers:Customers=new Customers();



  // Replace with your actual customer data

  currentPage = 1; // Current page number

  itemsPerPage = 5;





  constructor(private customerService: CustomerServiceService,

    private router: Router, private route: ActivatedRoute,

    private dialog: MatDialog) {



    this.customerId = +this.route.snapshot.params['customerId'];

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

  // allcustomers:any[];

  // constructor(private customerService:CustomerServiceService){}

  ngOnInit(): void {

    this.getCustomers();

  }


  private getCustomers() {

    this.customerService.getCustomersList().subscribe((data: any) => {
      console.log(data);
      this.customerService.setAllRecords(data);
      this.customers = data.filter((x: any) => x.role == "CUSTOMER");
    }, (error) => {
      this.customerService.throwError(error);
    }
    );







    // console.log(this.customers);

  }


  getPageStartIndex(): number {

    return (this.currentPage - 1) * this.itemsPerPage;

  }



  getPageEndIndex(): number {

    return this.getPageStartIndex() + this.itemsPerPage;

  }



  setCurrentPage(pageNumber: number): void {

    this.currentPage = pageNumber;

  }



  getPages(): number[] {

    const totalPages = Math.ceil(this.customers.length / this.itemsPerPage);

    return Array.from({ length: totalPages }, (_, index) => index + 1);

  }



  showPagination(): boolean {

    return this.customers.length > this.itemsPerPage;

  }

  getTotalPages(): number {

    return Math.ceil(this.customers.length / this.itemsPerPage);

  }



  updateCustomer(customer: Customers) {
    // Make sure customerId is not undefined before navigating
    console.log(customer);

    if (customer.customerId !== undefined) {

      console.log(customer.customerId);

      this.router.navigate(['update-customer', customer.customerId], {

        state: {

          mycustomerdata: customer

        }

      });

    }

    console.log(customer.customerId)

  }
  fetchAccounts(customerId) {
    this.router.navigate(['accounts', customerId])
  }






  openDeleteConfirmationDialog(customerId: number): void {

    const dialogRef = this.dialog.open(DeleteConfirmationDialogComponent, {

      width: '400px',
      data:{
        myString:"Customer ?"
      }
    });



    dialogRef.afterClosed().subscribe(result => {

      if (result === true) {

        this.customerService.deleteCustomer(customerId).subscribe(

          (data: any) => {

            console.log(data);

            this.getCustomers();
            this.alertMessage = 'Customer Deleted Successfully';
            this.showSuccessAlertFunc();

          },

          (error) => {

            console.error(error);



            if (error && error.error) {

              console.log(error.error);

            }

          }

        );

        console.log(customerId);

      }

    });

  }

}