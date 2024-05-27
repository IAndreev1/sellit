import {Component, OnInit} from '@angular/core';
import {FormsModule} from "@angular/forms";
import {NgForOf, NgIf} from "@angular/common";
import {NavbarComponent} from "../navbar/navbar.component";
import {ProductCardComponent} from "../products/product-card/product-card.component";
import {AllProductsService} from "../../services/all-products.service";
import {ToastrService} from "ngx-toastr";
import {Router} from "@angular/router";

@Component({
  selector: 'app-account',
  standalone: true,
  imports: [
    FormsModule,
    NgIf,
    NgForOf,
    NavbarComponent,
    ProductCardComponent
  ],
  templateUrl: './account.component.html',
  styleUrl: './account.component.scss'
})
export class AccountComponent implements OnInit {
  activeSection: string = 'viewProducts';
  newName: string = '';
  currentPassword: string = '';
  newPassword: string = '';
  products = [];


  constructor(private service: AllProductsService,
              private notification: ToastrService,
              private router: Router) {
  }

  ngOnInit(): void {
    this.loadProducts();
  }


  toggleSection(section: string) {
    this.activeSection = section;
  }

  changeName() {
    console.log('Name changed to:', this.newName);
  }

  changePassword() {
    console.log('Password changed to:', this.newPassword);
  }

  loadProducts() {
    this.service.getUserProducts().subscribe({
        next: res => {
          this.products = res;
        },
        error: error => {

        }
      }
    )
  }


  deleteProduct(productId: number) {
    this.products = this.products.filter(product => product.id !== productId);
    console.log('Product deleted:', productId);
  }

  redirectToProduct(productId: string) {
    this.router.navigate(['/' + productId + '/myProduct']);
  }
}
