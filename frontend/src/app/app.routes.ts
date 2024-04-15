import { Routes } from '@angular/router';
import {HomepageComponent} from "./components/homepage/homepage.component";
import {LoginComponentComponent} from "./components/login-component/login-component.component";
import {RegisterComponent} from "./components/register/register.component";
import {AboutComponent} from "./components/about/about.component";
import {ProductsComponent} from "./components/products/products.component";

export const routes: Routes = [
  {path: 'home', component: HomepageComponent},
  {path: 'login', component: LoginComponentComponent},
  {path: 'register', component: RegisterComponent},
  {path: 'about', component: AboutComponent},
  {path: 'products', component: ProductsComponent},
];
