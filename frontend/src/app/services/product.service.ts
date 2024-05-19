import {Injectable} from '@angular/core';
import {UserDetail} from "../dtos/auth-request";
import {HttpClient} from "@angular/common/http";
import {Globals} from "../global/globals";
import {ProductDto} from "../dtos/productDto";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  private baseUri: string = this.globals.backendUri + '/product';

  constructor(private http: HttpClient, private globals: Globals) {
  }

  createProduct(product: ProductDto): Observable<ProductDto> {
    console.log(product.name);
    console.log(product.description);
    console.log(product.price);
    console.log(product.imageData);

    return this.http.post<ProductDto>(this.baseUri, product);
  }

}
