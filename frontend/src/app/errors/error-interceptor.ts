import { HttpContextToken, HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { catchError, throwError } from 'rxjs';

export const HANDLED_ERROR_STATUSES = new HttpContextToken<number[]>(() => []);

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  const snackBar = inject(MatSnackBar);
  return next(req).pipe(
    catchError((response: HttpErrorResponse) => {
      const handledStatuses = req.context.get(HANDLED_ERROR_STATUSES);
      if (!handledStatuses.includes(response.status)) {
        snackBar.open(
          response.error?.detail ?? 'An unexpected error occurred. Please try again later.',
          'Close',
          { duration: 5000 },
        );
      }
      return throwError(() => response);
    }),
  );
};
