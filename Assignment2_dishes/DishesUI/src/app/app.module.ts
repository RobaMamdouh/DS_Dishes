import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import {HttpClientModule} from '@angular/common/http';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { RegisterComponent } from './auth/register/register.component';
import { LoginComponent } from './auth/login/login.component';
import { LandingPageComponent } from './landing-page/landing-page.component';
import { AdminDashboardComponent } from './admin/admin-dashboard/admin-dashboard.component';
import { UserDashboardComponent } from './user/user-dashboard/user-dashboard.component';
import { CreateCompanyComponent } from './admin/create-company/create-company.component';
import { ViewAllCompanyComponent } from './admin/view-all-company/view-all-company.component';
import { ViewAllUsersComponent } from './admin/view-all-users/view-all-users.component';
import { SellerDashboardComponent } from './company/seller-dashboard/seller-dashboard.component';
import { ViewSoldDishesComponent } from './company/view-sold-dishes/view-sold-dishes.component';
import { AddDishComponent } from './company/add-dish/add-dish.component';
import { UpdateDishComponent } from './company/update-dish/update-dish.component';
import { ViewDishesComponent } from './company/view-dishes/view-dishes.component';
import { CreateOrderComponent } from './user/create-order/create-order.component';
import { OrderListComponent } from './user/order-list/order-list.component';
import { ShoppingCartComponent } from './user/shopping-cart/shopping-cart.component';
import { OrderDetailComponent } from './user/order-detail/order-detail.component';
import { SoldDishDetailComponent } from './company/sold-dish-detail/sold-dish-detail.component';
import { PaymentComponent } from './admin/payment/payment.component';
import { LogsComponent } from './admin/logs/logs.component';



@NgModule({
  declarations: [
    AppComponent,
    RegisterComponent,
    LoginComponent,
    LandingPageComponent,
    AdminDashboardComponent,
    UserDashboardComponent,
    CreateCompanyComponent,
    ViewAllCompanyComponent,
    ViewAllUsersComponent,
    SellerDashboardComponent,
    ViewSoldDishesComponent,
    AddDishComponent,
    UpdateDishComponent,
    ViewDishesComponent,
    CreateOrderComponent,
    OrderListComponent,
    ShoppingCartComponent,
    OrderDetailComponent,
    SoldDishDetailComponent,
    PaymentComponent,
    LogsComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
