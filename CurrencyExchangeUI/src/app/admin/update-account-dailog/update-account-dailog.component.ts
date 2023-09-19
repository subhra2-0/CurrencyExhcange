import { HttpErrorResponse } from '@angular/common/http';
import { Component, Inject, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { MAT_MENU_PANEL, MatMenuPanel, MatMenuTrigger } from '@angular/material/menu';
import { ActivatedRoute, Router } from '@angular/router';
import { drCr_flgEnum } from 'src/app/admin/accounts';
import { AccountService } from 'src/app/service/account.service';


@Component({
  selector: 'app-update-account-dailog',
  templateUrl: './update-account-dailog.component.html',
  styleUrls: ['./update-account-dailog.component.css']
})
export class UpdateAccountDailogComponent {

  drCr_flg: typeof drCr_flgEnum = drCr_flgEnum;
  objectKeys = Object.keys;

  @ViewChild(MAT_MENU_PANEL) menu: MatMenuPanel<any>;

  accountForm: FormGroup;



  id: any;
  accId: any;
  showAmountAlert:boolean=false;
  validateBalance: any;
  
  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private fb: FormBuilder,
    public dialogRef: MatDialogRef<UpdateAccountDailogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private accountservice: AccountService
  ) {

    this.id = data.customer.customerId;
    console.log(data.accounts);
    if (data.accounts) {
      this.accId = data.accounts.accountNumber;
      console.log(this.accId);
    } else {
      console.log(data.customer.customerId);
    }



    this.accountForm = this.fb.group({
      amount: [null, Validators.required],
      drCr_flg: ['CREDIT', Validators.required]

    });
  }


  ngOnInit(): void {

  }
  menuItemClicked(option: string) {
    // Handle the menu item click here
    console.log(`Selected: ${option}`);
  }

  onCancelClick(): void {
    this.dialogRef.close();
  }


  onSubmit(): void {
    if (this.accountForm.valid) {
      this.submitAccountDetails();
      this.dialogRef.close(true);
    }
    else
    {
    
    }
  }

 
  submitAccountDetails() {
    console.log(this.accId);
    console.log(this.accountForm.value);

    this.accountservice.updateAccount(this.id, this.accId, this.accountForm.value)
      .subscribe(
        (data: any) => {
          console.log(data);
        },
        (error)=>
        {
         
        });

  }
  onAmountKeyUp()
  {

    if(this.accountForm.get('amount').value<0)
    {
      this.showAmountAlert =true;
      this.validateBalance = 'Negative.'
      this.accountForm.get('amount').setErrors({invalid:true})
      
    }
    else if(this.accountForm.get('amount').value==0)
    {
      this.showAmountAlert =true;
      this.validateBalance = 'Zero.'
      this.accountForm.get('amount').setErrors({invalid:true})
    }
    else  if(this.accountForm.get('amount').value==undefined)
    {
      this.showAmountAlert =true;
      this.validateBalance = 'Empty.'
      this.accountForm.get('amount').setErrors({invalid:true})
    }
    else
    {
      this.showAmountAlert =false;
      this.accountForm.get('amount').setErrors(null)
    }
  }


}
