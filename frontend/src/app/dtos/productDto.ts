import {UserDetail} from "./auth-request";

export class ProductDto {
  constructor(
    public id: number,
    public name: string,
    public description: string,
    public sold:boolean,
    public price: number,
    public user: UserDetail,
    public imageData: string,
  ) {
  }
}

export class ProductSearchDto {
  constructor(
    public name: string,
    public description: string,
    public priceFrom: number,
    public priceTo: number,
  ) {
  }
}
