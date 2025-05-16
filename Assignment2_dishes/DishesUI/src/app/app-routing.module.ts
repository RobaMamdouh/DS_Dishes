import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LandingPageComponent } from './landing-page/landing-page.component';
import { RegisterComponent } from './auth/register/register.component';
import { LoginComponent } from './auth/login/login.component';
import { UserDashboardComponent } from './user/user-dashboard/user-dashboard.component';
import { AdminDashboardComponent } from './admin/admin-dashboard/admin-dashboard.component';
import { SellerDashboardComponent } from './company/seller-dashboard/seller-dashboard.component';
import { ViewAllUsersComponent } from './admin/view-all-users/view-all-users.component';
import { ViewAllCompanyComponent } from './admin/view-all-company/view-all-company.component';
import { CreateCompanyComponent } from './admin/create-company/create-company.component';
import { UpdateDishComponent } from './company/update-dish/update-dish.component';
import { AddDishComponent } from './company/add-dish/add-dish.component';
import { ViewSoldDishesComponent } from './company/view-sold-dishes/view-sold-dishes.component';
import { ViewDishesComponent } from './company/view-dishes/view-dishes.component';



const routes: Routes = [
  { path: 'landingpage', component: LandingPageComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'login', component: LoginComponent },
  { path: 'user-home', component: UserDashboardComponent },
  { path: 'admin-home', component: AdminDashboardComponent },
  { path: 'seller-home', component: SellerDashboardComponent },
  { path: 'view-users', component: ViewAllUsersComponent },
  { path: 'view-companies', component: ViewAllCompanyComponent },
  { path: 'create-company-account', component: CreateCompanyComponent },
  { path: 'update-dish/:id', component: UpdateDishComponent },
  { path: 'add-dish', component: AddDishComponent },
  { path: 'view-sold-dishes', component: ViewSoldDishesComponent },
  { path: 'view-dishes', component: ViewDishesComponent },
  { path: '', redirectTo: '/landingpage', pathMatch: 'full' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
