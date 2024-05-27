import { UserDetail } from "./auth-request";
import { ProductDto } from "./productDto";

export class BetDto {
  constructor (
    public id?: number,
    public description?: string,
    public amount?: number,
    public date?:Date,
    public user?: UserDetail,
    public product?: ProductDto
  ) {}
}
