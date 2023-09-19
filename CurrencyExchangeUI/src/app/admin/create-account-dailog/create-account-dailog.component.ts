import { Component, Inject, OnInit, ViewChild  } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { MAT_MENU_PANEL, MatMenuPanel, MatMenuTrigger } from '@angular/material/menu';
import { ActivatedRoute, Router } from '@angular/router';

import { Accounts,CurrencyTypeEnum } from 'src/app/admin/accounts';
import { AccountService } from 'src/app/service/account.service';
import { Customers } from '../customers';

@Component({
  selector: 'app-create-account-dailog',
  templateUrl: './create-account-dailog.component.html',
  styleUrls: ['./create-account-dailog.component.css']
})

export class CreateAccountDailogComponent implements OnInit {

   // This variable holds the selected fruit
  currencyType = Object.values(CurrencyTypeEnum);

  @ViewChild(MAT_MENU_PANEL) menu: MatMenuPanel<any>;

  accountForm: FormGroup;
  
  

  id:any;
  accounts:Accounts[]=[];
  showNameAlert:boolean = false;
  showAmountAlert:boolean = false;
  showCurrencyAlert:boolean=false;
  
  constructor(
    private route:ActivatedRoute,
    private router:Router,
    private fb: FormBuilder,
    public dialogRef: MatDialogRef<CreateAccountDailogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private accountservice: AccountService
  ) 

  
  {
    this.id=data.customer.customerId;
    console.log(data.customer.customerId);
    this.accountForm = this.fb.group({
      balance: [0, Validators.min(0)],
      currencyType: [null, Validators.required],
      accHolderName: new FormControl(null, [
        Validators.required,
        Validators.pattern(/^[^\d]+$/)
      ])
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
    }
  }
  submitAccountDetails()
  {
      this.accountservice.createAccount(this.id,this.accountForm.value)
      .subscribe(
        (data:any)=>{
          this.dialogRef.close(true);
      },(error)=>{

        this.dialogRef.close(error);
      });
      
  }
  onAccountNameKeyUp()
  {
    if (this.accountForm.get('accHolderName').invalid) {
      this.showNameAlert = true;
      this.accountForm.get('accHolderName').setErrors({invalid:true})
    } else {
      this.showNameAlert = false;
      this.accountForm.get('accHolderName').setErrors(null);
    }
  }
  onCurrencyTypeKeyUp()
  {
    if(this.accountForm.get('currencyType').value==undefined)
    {
      this.showCurrencyAlert = true;
      this.accountForm.get('currencyType').setErrors({
        invalid:true
      })
    }
    else
    {
      this.showCurrencyAlert=false;
      this.accountForm.get('currencyType').setErrors(null);
    }
  }
  onAmountKeyUp()
  {
    if( this.accountForm.get('balance').value<0)
    {
      this.showAmountAlert=true;
      this.accountForm.get('balance').setErrors({
        invalid:true
      });
    }
    else
    {
      this.showAmountAlert = false;
      this.accountForm.get('balance').setErrors(null);
    }
  }
}
