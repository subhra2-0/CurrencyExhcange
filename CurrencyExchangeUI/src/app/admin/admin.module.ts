import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HomeComponent } from './home/home.component';
import { AppModule } from '../app.module';
import { AdminRoutingModule } from './admin-routing.module';
import { HeaderComponent } from '../components/header/header.component';
import {HttpClientModule} from '@angular/common/http';
// import { FormsHandlingComponent } from './forms-handling/forms-handling.component';
import { AddCustomerComponent } from './add-customer/add-customer.component';
import { FormBuilder, FormsModule } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import { AllCustomersComponent } from './all-customers/all-customers.component';
import { UpdateCustomerComponent } from './update-customer/update-customer.component';
import { MatButton, MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import{MatInputModule} from '@angular/material/input';
import{MatDialogModule} from '@angular/material/dialog';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatMenuModule } from '@angular/material/menu'; 
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
    







@NgModule({
  declarations: [
    HomeComponent,
    AddCustomerComponent,
    AllCustomersComponent,
    UpdateCustomerComponent
    
  ],
  imports: [
    CommonModule,
    BrowserAnimationsModule,
    MatMenuModule,
    AdminRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    MatButtonModule,
    MatIconModule,
    MatFormFieldModule,
    MatInputModule,
    MatDialogModule,
    MatToolbarModule
  ],



})
export class AdminModule { }
