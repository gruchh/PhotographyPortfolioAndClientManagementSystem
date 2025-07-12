import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';

interface ContactForm {
  name: string;
  email: string;
  phone: string;
  message: string;
  sessionType: string;
}

@Component({
  selector: 'app-contact-section',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './contact-section.html',
})
export class ContactSection implements OnInit {
  contactForm: FormGroup;
  isSubmitting = false;

  constructor(private fb: FormBuilder) {
    this.contactForm = this.fb.group({
      name: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      phone: [''],
      message: ['', Validators.required],
      sessionType: ['wedding', Validators.required]
    });
  }

  ngOnInit() {
  }

  onSubmit() {
    if (this.contactForm.valid) {
      this.isSubmitting = true;
      setTimeout(() => {
        console.log('Formularz kontaktowy wysłany:', this.contactForm.value);
        alert('Dziękujemy za wiadomość! Skontaktujemy się z Tobą wkrótce.');

        this.contactForm.reset({
          name: '',
          email: '',
          phone: '',
          message: '',
          sessionType: 'wedding'
        });

        this.isSubmitting = false;
      }, 1500);
    } else {
      this.markAllFieldsAsTouched();
    }
  }

  isFieldInvalid(fieldName: string): boolean {
    const field = this.contactForm.get(fieldName);
    return !!(field && field.invalid && (field.dirty || field.touched));
  }

  private markAllFieldsAsTouched() {
    Object.keys(this.contactForm.controls).forEach(key => {
      this.contactForm.get(key)?.markAsTouched();
    });
  }
}
