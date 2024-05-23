import {Component, OnInit} from '@angular/core';
import {NavbarComponent} from "../navbar/navbar.component";
import {ProductDto} from "../../dtos/productDto";
import {ProductService} from "../../services/product.service";
import {ToastrService} from "ngx-toastr";
import {ActivatedRoute, Router} from "@angular/router";
import {NgIf} from "@angular/common";

@Component({
  selector: 'app-single-product-view',
  standalone: true,
  imports: [
    NavbarComponent,
    NgIf
  ],
  templateUrl: './single-product-view.component.html',
  styleUrl: './single-product-view.component.scss'
})
export class SingleProductViewComponent implements OnInit {

  product: ProductDto;
  decodedImage: string;

  constructor(
    private service: ProductService,
    private notification: ToastrService,
    private router: Router,
    private route: ActivatedRoute,
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
}
