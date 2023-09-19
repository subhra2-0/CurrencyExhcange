import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { SignupComponent } from './components/signup/signup.component';
import { HomeComponent } from './admin/home/home.component';
import { OrderwatchlistComponent } from './order/orderwatchlist/orderwatchlist.component';
import { CreateAccountDailogComponent } from './admin/create-account-dailog/create-account-dailog.component';
import { PlaceOrderComponent } from './order/place-order/place-order.component';
import { AuthGuard } from './auth/auth.guard';

const routes: Routes = [

  {path:'login',component:LoginComponent},
  {path:'signup',component:SignupComponent},
  {path:'home', component:HomeComponent},
  
  ,
   {
    path:'placeOrder' ,canActivate:[AuthGuard],
    component:PlaceOrderComponent
  },
  {
    path:'orderwatchlist',canActivate:[AuthGuard],
     component:OrderwatchlistComponent
  },
  {path:'createAccountDialogComp', component:CreateAccountDailogComponent}


];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
