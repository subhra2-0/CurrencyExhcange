import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import{MatToolbarModule} from '@angular/material/toolbar'
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { LoginComponent } from './components/login/login.component';
import { SignupComponent } from './components/signup/signup.component';
// import { HomeComponent } from './admin/home/home.component';
import { HeaderComponent } from './components/header/header.component';
import { AdminModule } from './admin/admin.module';
import { FooterComponent } from './components/footer/footer.component';
// import { MatDialogActions } from '@angular/material/dialog';

//prudhvi's Modules
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatRadioModule } from '@angular/material/radio';
import { OrderwatchlistComponent } from './order/orderwatchlist/orderwatchlist.component';
import { PlaceOrderComponent } from './order/place-order/place-order.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatSelectModule } from '@angular/material/select';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatTableModule } from '@angular/material/table';
import { MAT_DIALOG_DATA, MatDialogModule,MatDialogRef } from '@angular/material/dialog';
import { HttpClientModule } from '@angular/common/http';
import { AccountListComponent } from './admin/account-list/account-list.component';
import { AccountDetailsComponent } from './admin/account-details/account-details.component';
import { CreateAccountDailogComponent } from './admin/create-account-dailog/create-account-dailog.component';
import { MatMenuModule } from '@angular/material/menu';
import { UpdateAccountDailogComponent } from './admin/update-account-dailog/update-account-dailog.component';


//

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    SignupComponent,
    HeaderComponent,
    FooterComponent,
    OrderwatchlistComponent,
    AccountListComponent,
    AccountDetailsComponent,
    CreateAccountDailogComponent,
    PlaceOrderComponent,
    UpdateAccountDailogComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    NgbModule,
    MatMenuModule,
    BrowserAnimationsModule,
    MatButtonModule,
    MatIconModule,
    AdminModule,
    MatToolbarModule,
    MatDialogModule,
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MatToolbarModule,
    MatButtonModule,
    MatIconModule,
    FormsModule,
    HttpClientModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatDialogModule,
    MatSelectModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatRadioModule,
    MatTableModule
  ],

  exports:[
    HeaderComponent
  ],
  providers: [
    {
      provide: MatDialogRef, useValue:{}
   } ,
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
  
 }
