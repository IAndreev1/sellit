import { Routes } from '@angular/router';
import {LoginComponent} from "./components/login/login.component";
import {RegisterComponent} from "./components/register/register.component";
import {HomeComponent} from "./components/home/home.component";
import {AddProductComponent} from "./components/add-product/add-product.component";
import {ProductsComponent} from "./components/products/products.component";
import {AccountComponent} from "./components/account/account.component";
import {SingleProductViewComponent} from "./components/single-product-view/single-product-view.component";
import {
  SingleProductEditDeleteViewComponent
} from "./components/single-product-edit-delete-view/single-product-edit-delete-view.component";

export const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'addNewProduct', component: AddProductComponent },
  { path: 'products', component: ProductsComponent },
  { path: 'account', component: AccountComponent },
  { path: 'product', component: SingleProductViewComponent },
  { path: 'myProduct', component: SingleProductEditDeleteViewComponent },


];
