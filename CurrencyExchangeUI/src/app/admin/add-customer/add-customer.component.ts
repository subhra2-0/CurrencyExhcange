import { Component, OnInit } from '@angular/core';
import {FormControl, FormGroup, FormsModule, Validators} from '@angular/forms'
import { CustomerServiceService } from 'src/app/service/customer-service.service';
import { Customers } from '../customers';
import { Router } from '@angular/router';
import { RoleEnum } from '../customers';
import { HttpErrorResponse } from '@angular/common/http';
import { AllCustomersComponent } from '../all-customers/all-customers.component';


@Component({
  selector: 'app-add-customer',
  templateUrl: './add-customer.component.html',
  styleUrls: ['./add-customer.component.css']
})
export class AddCustomerComponent implements OnInit{

  customers:Customers = new Customers();
  

  gender=['MALE','FEMALE'];
  role=['SUPERUSER','CUSTOMER']
  signUpForm:FormGroup;
  allRecords:Customers[]=[];

  showErrorMessageAlert:boolean= false;
  errorMessage: string | null = null;

  showforbiddenErrorAlert:boolean=false;
  forbiddenError: boolean = false;
  showFormInputAlert=false;
  showSuccessAlert: boolean = false;
  showUsernameAlert = false; 
  showEmailAlert = false;
  showPasswordAlert = false;
  showPhNumberAlert = false;


  showFormInputAlertFunc() {
    this.showFormInputAlert = true;
    setTimeout(() => {
      this.hideFormInputAlertFunc(); 
    }, 5000);
  }

  hideFormInputAlertFunc() {
    this.showFormInputAlert = false;
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

  onCustomerNameKeyUp() {
    if (this.signUpForm.get('customerName').invalid) {
      this.showUsernameAlert = true;
      this.signUpForm.get('customerName').setErrors({invalid:true})
    } else {
      this.showUsernameAlert = false;
      this.signUpForm.get('customerName').setErrors(null)
    }
  }
  onEmailKeyUp()
  {
    if(this.signUpForm.get('email').invalid) {
      this.showEmailAlert = true;
      this.errorMessage = 'Please enter valid Email';
      this.signUpForm.get('email').setErrors({invalid:true})
    }
    else if(this.allRecords.find((x) => x.email === this.signUpForm.get('email').value))
    {
      console.log(this.allRecords);
      
      this.showEmailAlert = true;
      this.errorMessage = 'Email is already Registered';
      this.signUpForm.get('email').setErrors({invalid:true})
    }
    else
    {
      this.showEmailAlert = false;
      this.signUpForm.get('email').setErrors(null)
    }
  }
  onPasswordKeyUp()
  {
    if(this.signUpForm.get('password').invalid){
      this.showPasswordAlert= true;
      this.signUpForm.get('password').setErrors({invalid:true})
    }
    else{
      this.showPasswordAlert= false;
      this.signUpForm.get('password').setErrors(null)
    }
  }
  onPhNumberKeyUp()
  {
    if(this.signUpForm.get('contactNumber').invalid ){
      this.showPhNumberAlert=true;
      this.signUpForm.get('contactNumber').setErrors({invalid:true})
    }
    else{
      this.showPhNumberAlert=false;
      this.signUpForm.get('contactNumber').setErrors(null)
    }
  }

  private emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;

  constructor(private customerService: CustomerServiceService, private router: Router) {
    this.customerService.allRecords$.subscribe(records => {
      this.allRecords = records;
    })
    this.signUpForm = new FormGroup({

      'role':new FormControl('CUSTOMER',[]),
   
      'customerName': new FormControl(null, [
        Validators.required,
        Validators.pattern(/^[^\d]+$/)
      ]),
      'email': new FormControl(null, [Validators.required, Validators.pattern(this.emailPattern)]),
      'password': new FormControl(null, [
        Validators.required,
        Validators.minLength(8), 
        Validators.pattern(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]+$/) // Strong password pattern
      ]),
      'contactNumber': new FormControl(null, [
        Validators.required,
        Validators.pattern(/^[0-9]+$/), 
      ]),
      'gender': new FormControl('MALE'),
    });
  }
  
    

  onSubmit(){
    if (this.signUpForm.valid) {  
       this.saveCustomer(); 
    }
    else
    {
      this.showFormInputAlertFunc();
    }
  }

  ngOnInit():void{
  }
  
  saveCustomer() {
    console.log(this.signUpForm.value);
    this.signUpForm.get('role').setValue(RoleEnum.CUSTOMER);
    this.customerService.createCustomer(this.signUpForm.value).subscribe(
      data => {
        console.log(data);
        this.showSuccessAlertFunc();
        
        // setTimeout(() => {
        //   this.goToCustomerList();
        // }, 5000);
      },(error) => {
          this.customerService.throwError(error);
          
          if(error instanceof HttpErrorResponse && error.status === 406)
          {
           this.showErrorAlertFunc('Email already registered');
          }
          else if(error instanceof HttpErrorResponse && error.status === 401)
          {
            this.showForbiddenErrorAlertFunc();
          }
      }

    );
  }

  goToCustomerList(){
    this.router.navigate(['/allcustomers']);
  }

  showErrorAlertFunc(mymessage:any)
  {
    this.showErrorMessageAlert = true;
    this.errorMessage = mymessage;
    setTimeout(() => {
      this.hideAlertFunc(); 
    }, 5000);
  }
  hideAlertFunc()
  {
    this.showErrorMessageAlert=false;
    this.errorMessage = null;
  }

  showForbiddenErrorAlertFunc()
  {
    this.showforbiddenErrorAlert = true;
    setTimeout(() => {
      this.hideForbiddenAlertFunc(); 
    }, 5000);
  }
  hideForbiddenAlertFunc()
  {
    this.showforbiddenErrorAlert=false;
  }
}
