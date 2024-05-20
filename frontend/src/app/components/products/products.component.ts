import {Component} from '@angular/core';
import {RouterLink} from "@angular/router";
import {ProductDto} from "../../dtos/productDto";
import {iterator} from "rxjs/internal/symbol/iterator";
import {ProductCardComponent} from "./product-card/product-card.component";
import {NgForOf, NgIf} from "@angular/common";
import {FormsModule} from "@angular/forms";
import {NavbarComponent} from "../navbar/navbar.component";

@Component({
  selector: 'app-products',
  standalone: true,
  imports: [
    RouterLink,
    ProductCardComponent,
    NgForOf,
    NgIf,
    FormsModule,
    NavbarComponent
  ],
  templateUrl: './products.component.html',
  styleUrl: './products.component.scss'
})
export class ProductsComponent {
  products: ProductDto[] = [];
  searchParameters: ProductDto = {
    id: null,
    name: '',
    description: '',
    price: 0,
    imageData: null
  };
  protected readonly iterator = iterator;

  loadStorage() {

  }
}
