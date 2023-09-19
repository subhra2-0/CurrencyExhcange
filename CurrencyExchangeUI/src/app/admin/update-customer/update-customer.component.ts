import { Component, OnInit } from '@angular/core';

import { FormBuilder, FormGroup, Validators } from '@angular/forms';

import { CustomerServiceService } from 'src/app/service/customer-service.service';

import { Customers, RoleEnum } from '../customers';

import { ActivatedRoute, Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';

@Component({

  selector: 'app-update-customer',

  templateUrl: './update-customer.component.html',

  styleUrls: ['./update-customer.component.css']

})

export class UpdateCustomerComponent implements OnInit {

  dialogueForm: FormGroup;

  customerId: number;

  customers: Customers = new Customers();

  gender = ['MALE', 'FEMALE'];

  customer = null;



  isUpdateSuccessful: boolean = false;
  showSuccessAlert: boolean = false;
  showErrorMessageAlert: boolean = false;
  errorMessage: string | null = null;

  showforbiddenErrorAlert: boolean = false;
  forbiddenError: boolean = false;
  showFormInputAlert = false;




  constructor(private fb: FormBuilder, private customerService: CustomerServiceService,
    private route: ActivatedRoute,
    private router: Router) {
    this.customerId = +this.route.snapshot.params['customerId'];
    this.customer = router.getCurrentNavigation().extras.state['mycustomerdata'];
    // this.customer = customer;
    console.log(this.customer);
    this.dialogueForm = this.fb.group({
      'role': [null, []],
      'customerName': [null, [
        Validators.required,
        Validators.pattern(/^[^\d]+$/)
      ]],
      'email': [null, [
        Validators.required,
        Validators.email
      ]],

      'contactNumber': [null, [
        Validators.required,
        Validators.pattern(/^\d{10}$/)
      ]],

      'gender': ['MALE', Validators.required],
    });

  }





  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const customerId = params.get('customerId');
      console.log(customerId);
      if (customerId && this.customer) {
        this.populateFormWithData(this.customer);
      }
    });
  }
  get email() {
    return this.dialogueForm.get('email')
  }

  validateUpdateForm() {
    if (!this.dialogueForm.valid) {
      this.dialogueForm.setErrors({
        invalid: true
      })
    }
    else {
      this.dialogueForm.setErrors(null)
    }
  }
  compareUpdatedDetails() {

  }

  showFormInputAlertFunc() {
    this.showFormInputAlert = true;
    setTimeout(() => {
      this.hideFormInputAlertFunc(); 
    }, 5000);
  }

  hideFormInputAlertFunc() {
    this.showFormInputAlert = false;
  }

  onSubmit() {
    if (this.dialogueForm.valid) {
      this.compareUpdatedDetails();
    } else {
      this.markFormControlsAsTouched(this.dialogueForm);
      this.showFormInputAlertFunc();
    }
    this.dialogueForm.get('role').setValue(RoleEnum.CUSTOMER);
    this.customerService.updateCustomer(this.customerId, this.dialogueForm.value).subscribe(data => {
      this.isUpdateSuccessful = true;
      //this.goToCustomerList();
      this.showSuccessAlertFunc();
    },error => {
      if(error instanceof HttpErrorResponse && error.status==403)
      {
        this.showErrorAlertFunc("Sesson Expired, Redirecting to Login");
      }
      else
        {
          this.showErrorAlertFunc(error.message);
        }
    })
  
  }



  goToCustomerList() {
    this.router.navigate(['/allcustomers']);
  }





  markFormControlsAsTouched(formGroup: FormGroup) {

    Object.values(formGroup.controls).forEach(control => {

      control.markAsTouched();



      if (control instanceof FormGroup) {

        this.markFormControlsAsTouched(control);

      }

    });

  }



  private populateFormWithData(data: Customers): void {

    this.dialogueForm.patchValue({

      'customerName': data.customerName,

      'email': data.email,

      'contactNumber': data.contactNumber,

      'gender': data.gender,

    });

  }


  showSuccessAlertFunc() {
    this.showSuccessAlert = true;
    setTimeout(() => {
      this.hideSuccessAlertFunc();
    }, 5000)
  }

  hideSuccessAlertFunc() {
    this.showSuccessAlert = false;
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
  showErrorAlertFunc(apiResponse: any) {
    this.showErrorMessageAlert = true;
    this.errorMessage = apiResponse;
    setTimeout(() => {
      this.hideErrorAlertFunc();
    }, 5000)
  }
  hideErrorAlertFunc()
  {
    this.showErrorMessageAlert=false;
    localStorage.clear();
    this.router.navigate(['login']);
  }

}

