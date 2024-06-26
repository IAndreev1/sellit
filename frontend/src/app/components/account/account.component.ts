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
import {AuthService} from "../../services/auth.service";
import {ChangePasswordDto, UserDetail} from "../../dtos/auth-request";
import {MatSnackBar, MatSnackBarHorizontalPosition, MatSnackBarVerticalPosition} from "@angular/material/snack-bar";

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
  passwords: ChangePasswordDto = {
    email:'',
    oldPassword:'',
    newPassword:''
  };
  user: UserDetail
  horizontalPosition: MatSnackBarHorizontalPosition = 'end';
  verticalPosition: MatSnackBarVerticalPosition = 'top';

  constructor(private service: AllProductsService,
              private notification: ToastrService,
              private router: Router,
              private betService: BetService,
              private authService: AuthService,
              private snackBar: MatSnackBar) {
  }

  ngOnInit(): void {
    this.loadProducts();
    this.loadBets();
    this.loadUser();
  }


  loadUser() {
    this.authService.getActiveUser().subscribe({
      next: res => {
        this.user = res;
      },
      error: error => {

      }
    })
  }

  toggleSection(section: string) {
    this.activeSection = section;
  }

  changeName() {
    console.log('Name changed to:', this.newName);
  }

  changePassword() {
    this.passwords.email = this.user.email;
    this.authService.changePassword(this.passwords).subscribe({
      next: res => {
        this.snackBar.open('Password changed successfully', 'Close', {
          duration: 3000,
          panelClass: ['snack-bar-success'],
          horizontalPosition: this.horizontalPosition,
          verticalPosition: this.verticalPosition,
        });
      },
      error: error => {
        this.snackBar.open('Error changing password', 'Close', {
          duration: 3000,
          panelClass: ['snack-bar-error'],
          horizontalPosition: this.horizontalPosition,
          verticalPosition: this.verticalPosition,
        });
      }
    });
  }

  loadProducts() {
    this.service.getUserProducts().subscribe({
        next: res => {
          this.products = res;
          console.log(" solddd" + this.products[1].sold)
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

  redirectToBetProd(bet:BetDto) {
    this.router.navigate([bet.product.id + '/product', ]);
  }

}
