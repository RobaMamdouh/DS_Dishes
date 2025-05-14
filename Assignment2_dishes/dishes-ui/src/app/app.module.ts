import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NavbarComponent } from './shared/navbar/navbar.component';
import { FooterComponent } from './shared/footer/footer.component';
import { LoginComponent } from './auth/login/login.component';
import { RegisterComponent } from './auth/register/register.component';
import { DashboardComponent } from './admin/dashboard/dashboard.component';
import { CreateSellerComponent } from './admin/create-seller/create-seller.component';
import { ViewCustomersComponent } from './admin/view-customers/view-customers.component';
import { ViewSellersComponent } from './admin/view-sellers/view-sellers.component';
import { AddDishComponent } from './seller/add-dish/add-dish.component';
import { UpdateDishComponent } from './seller/update-dish/update-dish.component';
import { ViewOrdersComponent } from './seller/view-orders/view-orders.component';
import { PlaceOrderComponent } from './customer/place-order/place-order.component';

@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    FooterComponent,
    LoginComponent,
    RegisterComponent,
    DashboardComponent,
    CreateSellerComponent,
    ViewCustomersComponent,
    ViewSellersComponent,
    AddDishComponent,
    UpdateDishComponent,
    ViewOrdersComponent,
    PlaceOrderComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
