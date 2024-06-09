import {Component, OnInit} from '@angular/core';
import {NavbarComponent} from "../navbar/navbar.component";
import {AllProductsService} from "../../services/all-products.service";
import {ToastrService} from "ngx-toastr";
import {Router} from "@angular/router";
import {ProductDto, ProductSearchDto} from "../../dtos/productDto";
import {ProductCardComponent} from "../products/product-card/product-card.component";
import {NgForOf, NgIf} from "@angular/common";
import {AuthService} from "../../services/auth.service";

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [
    NavbarComponent,
    ProductCardComponent,
    NgForOf,
    NgIf
  ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent implements OnInit {

  products: ProductDto[]

  searchParameters: ProductSearchDto = {
    name: '',
    description: '',
    priceFrom: 0,
    priceTo: 0,
  };

  constructor(private service: AllProductsService,
              private notification: ToastrService,
              private router: Router,
              public authService: AuthService) {
  }

  ngOnInit(): void {
    this.loadStorage()
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

  redirectToProduct(productId: string) {
    this.router.navigate(['/' + productId + '/product']);
  }
}
