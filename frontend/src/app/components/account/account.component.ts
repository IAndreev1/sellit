import { Component } from '@angular/core';
import {FormsModule} from "@angular/forms";
import {NgForOf, NgIf} from "@angular/common";
import {NavbarComponent} from "../navbar/navbar.component";
import {ProductCardComponent} from "../products/product-card/product-card.component";

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
export class AccountComponent {
  activeSection: string = 'changeName';
  newName: string = '';
  currentPassword: string = '';
  newPassword: string = '';
  products = [
    { id: 1, name: 'Product 1', price: 100 },
    { id: 2, name: 'Product 2', price: 200 },
    { id: 3, name: 'Product 3', price: 300 }
  ];

  toggleSection(section: string) {
    this.activeSection = section;
  }

  changeName() {
    // Implement change name logic here
    console.log('Name changed to:', this.newName);
  }

  changePassword() {
    // Implement change password logic here
    console.log('Password changed to:', this.newPassword);
  }

  deleteProduct(productId: number) {
    // Implement delete product logic here
    this.products = this.products.filter(product => product.id !== productId);
    console.log('Product deleted:', productId);
  }
}
