import {Component, OnInit} from '@angular/core';
import {RouterLink} from "@angular/router";
import {ProductDto, ProductSearchDto} from "../../dtos/productDto";
import {iterator} from "rxjs/internal/symbol/iterator";
import {ProductCardComponent} from "./product-card/product-card.component";
import {NgForOf, NgIf} from "@angular/common";
import {FormsModule} from "@angular/forms";
import {NavbarComponent} from "../navbar/navbar.component";
import {ToastrService} from "ngx-toastr";
import {AllProductsService} from "../../services/all-products.service";

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
export class ProductsComponent implements OnInit {
  products: ProductDto[] = [];
  searchParameters: ProductSearchDto = {
    name: '',
    description: '',
    priceFrom: 0,
    priceTo: 0,
  };


  constructor(private service: AllProductsService,
              private notification: ToastrService) {
  }

  ngOnInit(): void {
    console.log("init")
    this.loadStorage();
  }


  public loadStorage() {
    this.service.getItems(this.searchParameters).subscribe({
        next: res => {
          this.products = res;
        },
        error: error => {

        }
      }
    )
  }
}
