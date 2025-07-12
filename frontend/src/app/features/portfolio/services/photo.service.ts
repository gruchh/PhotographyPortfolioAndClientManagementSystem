import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { environment } from "../../../../environments/environment";
import { HttpClient, HttpParams } from "@angular/common/http";
import { PaginatedResponse, Photo } from "../../../core/models/photo.model";

@Injectable({
  providedIn: 'root'
})
export class PhotoService {
  private readonly API_URL = `${environment.apiUrl}/api/photos`;

  constructor(private http: HttpClient) {}

  getPhotos(page: number = 0, size: number = 12, publicOnly: boolean = true): Observable<PaginatedResponse<Photo>> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    if (publicOnly) {
      params = params.set('publiclyVisible', 'true');
    }

    return this.http.get<PaginatedResponse<Photo>>(this.API_URL, { params });
  }

  getFeaturedPhotos(): Observable<Photo[]> {
    return this.http.get<Photo[]>(`${this.API_URL}/featured`);
  }

  getPhotoById(id: number): Observable<Photo> {
    return this.http.get<Photo>(`${this.API_URL}/${id}`);
  }

  uploadPhoto(formData: FormData): Observable<Photo> {
    return this.http.post<Photo>(this.API_URL, formData);
  }

  updatePhoto(id: number, photoData: Partial<Photo>): Observable<Photo> {
    return this.http.put<Photo>(`${this.API_URL}/${id}`, photoData);
  }

  deletePhoto(id: number): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/${id}`);
  }
}
