import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { OrderRoutingModule } from './order-routing.module';
import { OrderwatchlistComponent } from './orderwatchlist/orderwatchlist.component';
import { PlaceOrderComponent } from './place-order/place-order.component';
import { ReactiveFormsModule } from '@angular/forms';
import { ConversionSuccessDialogComponent } from './conversion-success-dialog/conversion-success-dialog.component';


@NgModule({
  declarations: [
    

  
  
  
    ConversionSuccessDialogComponent
  ],
  imports: [

    CommonModule,
    ReactiveFormsModule,
    OrderRoutingModule
  ]
})
export class OrderModule { }
