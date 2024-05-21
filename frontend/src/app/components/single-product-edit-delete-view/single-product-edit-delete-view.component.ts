import {Component, OnInit} from '@angular/core';
import {ProductService} from "../../services/product.service";
import {ToastrService} from "ngx-toastr";
import {ActivatedRoute, Router} from "@angular/router";
import {ProductDto} from "../../dtos/productDto";
import {NavbarComponent} from "../navbar/navbar.component";
import {NgIf} from "@angular/common";

@Component({
  selector: 'app-single-product-edit-delete-view',
  standalone: true,
  imports: [
    NavbarComponent,
    NgIf
  ],
  templateUrl: './single-product-edit-delete-view.component.html',
  styleUrl: './single-product-edit-delete-view.component.scss'
})
export class SingleProductEditDeleteViewComponent implements OnInit {

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
    this.route.params.subscribe({
      next: params => {
        const prodId = params.id;
        this.service.getById(prodId).subscribe({
          next: res => {
            this.product = res;
            this.decodeImage();
          },
          error: error => {
            this.router.navigate(['/account'])
          }
        })
      },
      error: () => {
        this.router.navigate(['/account']);
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
