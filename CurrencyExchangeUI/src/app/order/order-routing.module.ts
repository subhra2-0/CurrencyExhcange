import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { PlaceOrderComponent } from './place-order/place-order.component';
import { OrderwatchlistComponent } from './orderwatchlist/orderwatchlist.component';
import { AuthGuard } from '../auth/auth.guard';
import { CommonModule } from '@angular/common';
const routes: Routes = 
[
  { path: '', redirectTo: 'login', pathMatch: 'full' },

  {
    path:'orderwatchlist',canActivate:[AuthGuard],
    component:OrderwatchlistComponent},
   {
    path:'placeOrder' ,canActivate:[AuthGuard],
    component:PlaceOrderComponent
  }

   
];

@NgModule({
  declarations: [],
  imports: [RouterModule.forChild(routes),
    CommonModule
  ],
  exports: [RouterModule]
})
export class OrderRoutingModule { }
