import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LandingPageComponent } from './landing-page/landing-page.component';
import { RegisterComponent } from './auth/register/register.component';
import { LoginComponent } from './auth/login/login.component';
import { UserDashboardComponent } from './user/user-dashboard/user-dashboard.component';
import { AdminDashboardComponent } from './admin/admin-dashboard/admin-dashboard.component';
import { SellerDashboardComponent } from './seller/seller-dashboard/seller-dashboard.component';


const routes: Routes = [
  {path: '', redirectTo: '/landingpage', pathMatch: 'full'},
   {path: 'landingpage', component: LandingPageComponent},
  {path: 'register', component: RegisterComponent},
  {path: 'login', component: LoginComponent},
  {path: 'user-home', component: UserDashboardComponent},
  {path: 'admin-home', component: AdminDashboardComponent},
  {path: 'seller-home', component: SellerDashboardComponent},

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
