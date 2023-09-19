import { Component, OnInit } from '@angular/core';
import { CustomerServiceService } from 'src/app/service/customer-service.service';
import { FormBuilder, FormGroup,ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute,Router } from '@angular/router';
import { LoaderService } from 'src/app/service/loader.service';
import {timer,take} from 'rxjs';
import { HttpStatusCode } from 'axios';
import Swal from 'sweetalert2';
import { HttpErrorResponse } from '@angular/common/http';
export class UserCredentials{

}

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})



export class LoginComponent implements OnInit  {


  showErrorMessageAlert:boolean= false;
  errorMessage: string | null;
  showEmailAlert:boolean =false;
  loginForm: FormGroup;
  constructor(private service:CustomerServiceService,
    private fb: FormBuilder,private router:Router,private route:ActivatedRoute,
    public loaderservice: LoaderService
    ){
      
     
    }

    get email(){
     return this.loginForm.get('email'); 
    }

    get password(){
      return this.loginForm.get('password');
    }
 
 

     private emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;


    ngOnInit(): void {
      this.loginForm = this.fb.group({
        email: [null, [Validators.required, Validators.pattern(this.emailPattern)]],
        password: ['', [Validators.required]]
      });
    }


    onEmailKeyUp()
    {
      if(this.loginForm.get('email').invalid) {
        this.showEmailAlert = true;
        this.errorMessage = 'Please enter valid Email';
        this.loginForm.get('email').setErrors({invalid:true})
      }
      else
      {
        this.showEmailAlert = false;
        this.loginForm.get('email').setErrors(null)
      }
    }
  

    onSubmit() {
      this.loaderservice.showLoader();
      timer(1500)
      .pipe(take(1))
      .subscribe(() => {
        this.loaderservice.hideLoader();
      if (this.loginForm.valid) {
        // Perform your login logic here
        this.service.login(this.loginForm.value).subscribe((dataResponse)=> {
          Swal.fire({

            icon: 'success',
    
            text: 'Login Successful',
    
            timer:1000,
    
            showConfirmButton: false,
    
          })
          console.log(dataResponse.token);
          console.log(dataResponse.role);
          console.log(dataResponse);

          localStorage.setItem("loginstatus","success");
          localStorage.setItem("token",dataResponse.token);
          localStorage.setItem("id",dataResponse.id);
          localStorage.setItem("role",dataResponse.role);
          
          console.log(localStorage);
          if(dataResponse.role==="SUPERUSER")
          {
            this.router.navigate(['allcustomers']);
          }
          else if(dataResponse.role==="CUSTOMER")
          {
            this.router.navigate(['placeOrder']);
          } 
        },

        (error) => {
          if(error instanceof HttpErrorResponse && error.status === 401)
          {
            this.showErrorAlertFunc()
          }
        }
      

        )

        

        
      }
    })
    }
    
    showErrorAlertFunc()
    {
   
      this.showErrorMessageAlert = true;
      this.errorMessage = 'Invalid Credentials!';
      setTimeout(() => {
        this.hideAlertFunc(); // Hide the alert after the timeout
      }, 5000);
    }
    hideAlertFunc()
    {
      this.showErrorMessageAlert=false;
      this.errorMessage = null;
    }
  
  
  
  // submit(){
  //   this.service.loggedIn=true
  // }

}
