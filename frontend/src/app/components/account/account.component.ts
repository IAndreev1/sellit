import {Component, OnInit} from '@angular/core';
import {FormsModule} from "@angular/forms";
import {DatePipe, NgForOf, NgIf} from "@angular/common";
import {NavbarComponent} from "../navbar/navbar.component";
import {ProductCardComponent} from "../products/product-card/product-card.component";
import {AllProductsService} from "../../services/all-products.service";
import {ToastrService} from "ngx-toastr";
import {Router} from "@angular/router";
import {BetCardComponent} from "../single-product-edit-delete-view/bet-card/bet-card.component";
import {MyBetCardComponent} from "./my-bet-card/my-bet-card.component";
import {BetService} from "../../services/bet.service";
import {BetDto} from "../../dtos/betDto";

@Component({
  selector: 'app-account',
  standalone: true,
  imports: [
    FormsModule,
    NgIf,
    NgForOf,
    NavbarComponent,
    ProductCardComponent,
    DatePipe,
    BetCardComponent,
    MyBetCardComponent
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
  bets = [];


  constructor(private service: AllProductsService,
              private notification: ToastrService,
              private router: Router,
              private betService: BetService) {
  }

  ngOnInit(): void {
    this.loadProducts();
    this.loadBets();
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

  loadBets() {
    this.betService.getAllBetsOfUser().subscribe({
        next: res => {
          this.bets = res;
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

  deleteBet(bet: BetDto): void {
    this.betService.deleteBet(bet).subscribe({
        next: res => {

        },
        error: error => {

        }
      }
    )




    // Add your logic to reject the bet here
    this.notification.error('Bet rejected successfully.');
    // Optionally, remove the bet from the list or update its status
    this.bets = this.bets.filter(b => b !== bet);
  }
}
