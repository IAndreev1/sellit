import {Component, OnInit} from '@angular/core';
import {ProductDto} from "../../../dtos/productDto";
import {ActivatedRoute, Router} from "@angular/router";
import {ProductService} from "../../../services/product.service";
import {FormsModule, NgForm} from "@angular/forms";
import {NgIf} from "@angular/common";
import {NavbarComponent} from "../../navbar/navbar.component";

@Component({
  selector: 'app-edit-product',
  standalone: true,
  imports: [
    FormsModule,
    NgIf,
    NavbarComponent
  ],
  templateUrl: './edit-product.component.html',
  styleUrl: './edit-product.component.scss'
})
export class EditProductComponent implements OnInit {
  product: ProductDto = {
    id: 0,
    name: "",
    description: "",
    sold:false,
    price: 0,
    user:null,
    imageData: null
  };
  imagePreview: string | null = null; // Property for image preview
  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private productService: ProductService
  ) {
  }

  ngOnInit(): void {
    const productId = this.route.snapshot.paramMap.get('id');
    this.productService.getById(productId).subscribe(product => {
      this.product = product;
      if (product.imageData != null) {
        this.imagePreview = product.imageData;
        this.decodeImage();
      }
    });
  }

  onFileSelected(event: Event): void {
    const fileInput = event.target as HTMLInputElement;
    if (fileInput.files && fileInput.files[0]) {
      const file = fileInput.files[0];
      const reader = new FileReader();
      reader.onload = () => {
        const base64String = reader.result as string;
        this.imagePreview = base64String; // Set the image preview
        this.product.imageData = base64String.split(',')[1]; // Remove the MIME type prefix
      };
      reader.readAsDataURL(file);
    }
  }

  onSubmit(form: NgForm): void {
    this.productService.update(this.product).subscribe(() => {
      this.router.navigate(['/account']);
    });
  }

  decodeImage(): void {
    if (this.imagePreview != null) {
      if (!this.imagePreview.startsWith('data:image')) {
        this.imagePreview = 'data:image/jpeg;base64,' + this.imagePreview;
      }
    }
  }
}
