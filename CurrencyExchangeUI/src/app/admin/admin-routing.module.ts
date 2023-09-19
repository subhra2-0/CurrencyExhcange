import { Component, NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { AddCustomerComponent } from './add-customer/add-customer.component';
import { AllCustomersComponent } from './all-customers/all-customers.component';
import { UpdateCustomerComponent } from './update-customer/update-customer.component';
import { AccountListComponent } from './account-list/account-list.component';
import { AuthGuard } from '../auth/auth.guard';
// import { RouterModule } from '@angular/router';

const routes: Routes=[
  {
    path:'add-customer',canActivate:[AuthGuard],
    component:AddCustomerComponent
  },
  {
    path:'allcustomers',canActivate:[AuthGuard],
    component:AllCustomersComponent
  },
  {
    path:'accounts/:customerId',canActivate:[AuthGuard],
    component:AccountListComponent
  },
  {
    path:'',redirectTo:'login',pathMatch:'full'
  },
  {
    path:'update-customer/:customerId',canActivate:[AuthGuard],
    component:UpdateCustomerComponent
  }
]



@NgModule({
  declarations: [],
  imports: [ RouterModule.forChild(routes),
    CommonModule
  ],
  exports:[RouterModule]
})
export class AdminRoutingModule { }
