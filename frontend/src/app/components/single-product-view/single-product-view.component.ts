import {Component, OnInit} from '@angular/core';
import {NavbarComponent} from "../navbar/navbar.component";
import {ProductDto} from "../../dtos/productDto";
import {ProductService} from "../../services/product.service";
import {ToastrService} from "ngx-toastr";
import {ActivatedRoute, Router} from "@angular/router";
import {NgIf} from "@angular/common";
import {BetDto} from "../../dtos/betDto";
import {BetService} from "../../services/bet.service";
import {FormsModule} from "@angular/forms";
import {MatSnackBar, MatSnackBarHorizontalPosition, MatSnackBarVerticalPosition} from "@angular/material/snack-bar";

@Component({
  selector: 'app-single-product-view',
  standalone: true,
  imports: [
    NavbarComponent,
    NgIf,
    FormsModule
  ],
  templateUrl: './single-product-view.component.html',
  styleUrl: './single-product-view.component.scss'
})
export class SingleProductViewComponent implements OnInit {

  product: ProductDto;
  decodedImage: string;
  bet:BetDto = {
    id:null,
    description:'',
    amount:0,
    user:null,
    product:null
  };
  userHasBet:boolean = false;
  horizontalPosition: MatSnackBarHorizontalPosition = 'end';
  verticalPosition: MatSnackBarVerticalPosition = 'top';
  constructor(
    private service: ProductService,
    private betService:BetService,
    private notification: ToastrService,
    private router: Router,
    private route: ActivatedRoute,
    private snackBar:  MatSnackBar
  ) {
  }


  ngOnInit(): void {
    console.log("onInit")
    this.route.params.subscribe({

      next: params => {
        console.log("param")
        const prodId = params.id;
        this.service.getById(prodId).subscribe({
          next: res => {

            this.product = res;
            console.log("prod")
            this.decodeImage();
          },
          error: error => {
           // this.router.navigate(['/products'])
          }
        })
      },
      error: () => {
      //  this.router.navigate(['/products']);
      }
    });
  }

  decodeImage(): void {
    if (this.product.imageData != null) {
      if (!this.product.imageData.startsWith('data:image')) {
        this.decodedImage = 'data:image/jpeg;base64,' + this.product.imageData;
      }
    }
  }

  onSubmit(){
    this.bet.product = this.product;
    this.betService.createBet(this.bet).subscribe({
      next: () => {
        this.snackBar.open('Bet successfully places', 'Close', {
          horizontalPosition: this.horizontalPosition,
          verticalPosition: this.verticalPosition,
          duration:3000
        });
        this.router.navigate(['/products']);

      },
      error: (error) => {

      }
    });
  }
}
